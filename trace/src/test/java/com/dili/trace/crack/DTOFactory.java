package com.dili.trace.crack;

import com.dili.ss.dto.*;
import com.dili.ss.exception.ParamErrorException;
import com.dili.ss.java.B;
import com.dili.ss.util.POJOUtils;
import javassist.*;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * DTO工厂
 */
public class DTOFactory implements IDTOFactory {

    /**
     * 静态内部类实现单例模式（线程安全，调用效率高，可以延时加载）
     */
    private static class DTOFactoryInstance {
        private static final DTOFactory instance = new DTOFactory();
    }

    private DTOFactory() {
    }

    public static DTOFactory getInstance() {
        return DTOFactoryInstance.instance;
    }

    static ClassPool classPool = ClassPool.getDefault();

    static {
        classPool.insertClassPath(new ClassClassPath(DTOFactory.class));
        classPool.importPackage("java.util.Map");
        classPool.importPackage("java.util.List");
        classPool.importPackage("java.util.HashMap");
        classPool.importPackage("java.lang.reflect.Field");
    }

    @Override
    public void insertClassPath(ClassPath classPath){
        classPool.insertClassPath(classPath);
    }

    @Override
    public void importPackage(String packageName){
        classPool.importPackage(packageName);
    }

    @Override
    @SuppressWarnings(value = {"unchecked", "deprecation"})
    public void registerDTOInstance(AnnotationMetadata importingClassMetadata) {
        Map attributes = importingClassMetadata.getAnnotationAttributes(DTOScan.class.getCanonicalName());
        Set<String> packages = this.getBasePackages(attributes, importingClassMetadata.getClassName());
        String file = (String) attributes.get("file");
        this.registerDTOInstanceFromPackages(packages, file);
    }

    /**
     * 从指定的包注册DTO实例
     */
    @Override
    @SuppressWarnings(value = {"unchecked", "deprecation"})
    public void registerDTOInstanceFromPackages(Set<String> packages, String file) {
        Reflections reflections = new Reflections(packages);
        Set<Class<? extends IDTO>> classes = reflections.getSubTypesOf(IDTO.class);
        if (classes != null) {
            DTOInstance.useInstance = true;
            for (Class<? extends IDTO> dtoClass : classes) {
                if (!dtoClass.isInterface()) {
                    continue;
                }
                try {
                    CtClass ctClass = this.createCtClass(dtoClass);
                    if (StringUtils.isNotBlank(file)) {
                        ctClass.writeFile(file);
                    }
                    DTOInstance.cache.put(dtoClass, (Class) ctClass.toClass());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        this.sss();
    }


    /**
     * 创建DTO接口的CtClass
     *
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    @Override
    @SuppressWarnings(value = {"unchecked", "deprecation"})
    public <T extends IDTO> CtClass createCtClass(Class<T> clazz) throws Exception {
        CtClass ctClass = null;
        if (!clazz.isInterface()) {
            throw new ParamErrorException("参数必须是接口");
        }
        if (!IDTO.class.isAssignableFrom(clazz)) {
            throw new ParamErrorException("参数必须实现IDTO接口");
        }
        if (IBaseDomain.class.isAssignableFrom(clazz)) {
            ctClass = this.createBaseDomainCtClass((Class<IBaseDomain>) clazz);
        } else if (IStringDomain.class.isAssignableFrom(clazz)) {
            ctClass = this.createStringDomainCtClass((Class<IStringDomain>) clazz);
        } else {
            ctClass = this.createDTOCtClass(clazz);
        }
        return this.createDynaCtClass(clazz, ctClass);
    }

    /**
     * 动态递归创建CtClass
     *
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    @SuppressWarnings(value = {"unchecked", "deprecation"})
    private <T extends IDTO> CtClass createDynaCtClass(Class<T> clazz, CtClass ctClass) throws Exception {
        //IBaseDomain、IStringDomain或IDTO已经加载， IDomain不支持
        if (clazz.equals(IBaseDomain.class) || clazz.equals(IDTO.class) || clazz.equals(IStringDomain.class) || clazz.equals(IDomain.class)) {
            return ctClass;
        }
        //非IDTO子类不加载
        if (!IDTO.class.isAssignableFrom(clazz)) {
            return ctClass;
        }
        //获取当前类的所有方法
        Method[] declaredMethods = clazz.getDeclaredMethods();
        //先添加Field
        for (Method method : declaredMethods) {
            String fieldName = POJOUtils.getBeanField(method);
            if (POJOUtils.isGetMethod(method)) {
                try {
                    //找不到属性抛异常,继续添加属性
                    ctClass.getDeclaredField(fieldName);
                    //找到属性就continue
                    continue;
                } catch (NotFoundException e) {
                }
                //构建属性
                StringBuilder fieldStringBuilder = new StringBuilder();
                //默认get方法的返回值赋给属性
                if(method.isDefault()){
                    fieldStringBuilder.append("private ").append(method.getReturnType().getTypeName()).append(" ")
                            .append(fieldName).append(" = ").append("(")
                            .append(method.getReturnType().getTypeName())
                            .append(")com.dili.ss.util.ReflectionUtils.invokeDefaultMethod(this, this.getClass().getInterfaces()[0].getMethod(\"")
                            .append(method.getName()).append("\", null), null);");
                }else {
                    fieldStringBuilder.append("private ").append(method.getReturnType().getTypeName()).append(" ").append(fieldName).append(";").toString();
                }
                CtField ctField = CtField.make(fieldStringBuilder.toString(), ctClass);
                ctClass.addField(ctField);

            }
        }

        for (Method method : declaredMethods) {
            //方法不能重复，不然编译报错
            if (ctClass.getDeclaredMethods(method.getName()).length > 0) {
                continue;
            }
            String fieldName = POJOUtils.getBeanField(method);
            if (POJOUtils.isGetMethod(method)) {
                //基础类型无法提供代理对象的值，因为无法区分是0还是null
                if (method.getReturnType().isPrimitive()) {
                    CtMethod method1 = CtMethod.make(new StringBuilder().append("public ").append(method.getReturnType().getTypeName()).append(" ").append(method.getName()).append("(){return this.").append(fieldName).append(";}").toString(), ctClass);
                    ctClass.addMethod(method1);
                }
//                else if (method.isDefault()) {
//                    String content = new StringBuilder().append("public ")
//                            .append(method.getReturnType().getTypeName()).append(" ")
//                            .append(method.getName()).append("(){").append("try {")
//                            .append("return this.").append(fieldName)
//                            .append(" == null ? $delegate.get(\"").append(fieldName)
//                            .append("\") == null ?").append("(")
//                            .append(method.getReturnType().getTypeName())
//                            .append(")com.dili.ss.util.ReflectionUtils.invokeDefaultMethod(this, this.getClass().getInterfaces()[0].getMethod(\"")
//                            .append(method.getName()).append("\", null), null)")
//                            .append(":(").append(method.getReturnType().getTypeName())
//                            .append(")$delegate.get(\"").append(fieldName).append("\")")
//                            .append(" : this.").append(fieldName).append(";")
//                            .append("}catch (Throwable e){").append("return null;")
//                            .append("}").append("}").toString();
//                    CtMethod method1 = CtMethod.make(content, ctClass);
//                    ctClass.addMethod(method1);
//                }
                else {
                    CtMethod method1 = CtMethod.make(new StringBuilder().append("public ").append(method.getReturnType().getTypeName()).append(" ").append(method.getName()).append("(){return this.").append(fieldName).append(" == null ? (").append(method.getReturnType().getTypeName()).append(")$delegate.get(\"").append(fieldName).append("\") : this.").append(fieldName).append(";}").toString(), ctClass);
                    ctClass.addMethod(method1);
                }

            } else if (POJOUtils.isSetMethod(method)) {
                String methodStr;
                //基本类型无法判断null
                if(method.getParameterTypes()[0].isPrimitive()){
                    methodStr = new StringBuilder().append("public void ").append(method.getName()).append("(").append(method.getParameterTypes()[0].getTypeName()).append(" ").append(fieldName).append("){this.").append(fieldName).append(" = ").append(fieldName).append(";}").toString();
                }else{
                    methodStr = new StringBuilder().append("public void ").append(method.getName()).append("(").append(method.getParameterTypes()[0].getTypeName()).append(" ").append(fieldName).append("){if(").append(fieldName).append(" == null){this.aset(\"").append(fieldName).append("\", null);}this.").append(fieldName).append(" = ").append(fieldName).append(";}").toString();
                }
                CtMethod method1 = CtMethod.make(methodStr, ctClass);
                ctClass.addMethod(method1);
            }
        }
        Class<?>[] interfaces = clazz.getInterfaces();
        if (interfaces != null) {
            for (int i = 0; i < interfaces.length; i++) {
                this.createDynaCtClass((Class) interfaces[i], ctClass);
            }
        }
        return ctClass;
    }

    @SuppressWarnings(value = {"unchecked", "deprecation"})
    private <T extends IDTO> CtClass createDTOCtClass(Class<T> clazz) throws Exception {
        CtClass intfCtClass = classPool.get(clazz.getName());
        CtClass implCtClass = classPool.makeClass(clazz.getName() + DTOInstance.SUFFIX);
        implCtClass.setInterfaces(new CtClass[]{intfCtClass});
        CtField $delegateField = CtField.make("private com.dili.ss.dto.DTO $delegate;", implCtClass);
        implCtClass.addField($delegateField);
        CtConstructor defaultConstructor = new CtConstructor(null, implCtClass);
        defaultConstructor.setModifiers(Modifier.PUBLIC);
        defaultConstructor.setBody("{this.$delegate = new com.dili.ss.dto.DTO();}");
        implCtClass.addConstructor(defaultConstructor);

        //添加fields属性，存储当前类和父类所有属性,key为属性名
        implCtClass.addField(CtField.make("private Map fields = null;", implCtClass));
        CtMethod getFieldsMethod = CtMethod.make(new StringBuilder().append("public Map getFields(){").append("if(fields == null){").append("fields = new HashMap();").append("List fieldList = org.apache.commons.lang3.reflect.FieldUtils.getAllFieldsList(getClass());").append("int size = fieldList.size();").append("for(int i=0; i<size; i++){").append("java.lang.reflect.Field field = (java.lang.reflect.Field)fieldList.get(i);").append("fields.put(field.getName(), field);").append("}").append("}").append("return fields;}").toString(), implCtClass);
        implCtClass.addMethod(getFieldsMethod);

        CtMethod agetMethod = CtMethod.make("public Object aget(String property){return $delegate.get(property);}", implCtClass);
        CtMethod agetAllMethod = CtMethod.make("public com.dili.ss.dto.DTO aget(){return $delegate;}", implCtClass);

        CtMethod asetMethod = CtMethod.make(new StringBuilder().append("public void aset(String property, Object value){").append("Field field = (Field)getFields().get(property);").append("if(field != null){try{field.set(this, value);}catch(Exception e){field.set(this, null);}}").append("this.$delegate.put(property, value);}").toString(), implCtClass);
        CtMethod asetAllMethod = CtMethod.make("public void aset(com.dili.ss.dto.DTO dto){this.$delegate = dto;}", implCtClass);
        implCtClass.addMethod(agetMethod);
        implCtClass.addMethod(agetAllMethod);
        implCtClass.addMethod(asetMethod);
        implCtClass.addMethod(asetAllMethod);

        CtMethod mgetMethod = CtMethod.make("public Object mget(String property){return this.$delegate.getMetadata(property);}", implCtClass);
        CtMethod mgetAllMethod = CtMethod.make("public java.util.Map mget(){return this.$delegate.getMetadata();}", implCtClass);
        CtMethod msetMethod = CtMethod.make("public void mset(String property, Object value){this.$delegate.setMetadata(property, value);}", implCtClass);
        CtMethod msetAllMethod = CtMethod.make("public void mset(java.util.Map metadata){this.$delegate.setMetadata(metadata);}", implCtClass);
        implCtClass.addMethod(mgetMethod);
        implCtClass.addMethod(mgetAllMethod);
        implCtClass.addMethod(msetMethod);
        implCtClass.addMethod(msetAllMethod);

        return implCtClass;
    }

    @SuppressWarnings(value = {"unchecked", "deprecation"})
    private <T extends IDomain> CtClass createDomainCtClass(Class<T> clazz) throws Exception {
        CtClass implCtClass = this.createDTOCtClass(clazz);
        //创建属性
        CtField pageField = CtField.make("private Integer page;", implCtClass);
        CtField rowsField = CtField.make("private Integer rows;", implCtClass);
        CtField sortField = CtField.make("private String sort;", implCtClass);
        CtField orderField = CtField.make("private String order;", implCtClass);

        implCtClass.addField(pageField);
        implCtClass.addField(rowsField);
        implCtClass.addField(sortField);
        implCtClass.addField(orderField);

        CtMethod getPageMethod = CtMethod.make("public Integer getPage(){return page==null?(Integer)aget(\"page\"):page;}", implCtClass);
        CtMethod setPageMethod = CtMethod.make("public void setPage(Integer page){this.page=page;}", implCtClass);

        CtMethod getRowsMethod = CtMethod.make("public Integer getRows(){return rows==null?(Integer)aget(\"rows\"):rows;}", implCtClass);
        CtMethod setRowsMethod = CtMethod.make("public void setRows(Integer rows){this.rows=rows;}", implCtClass);

        CtMethod getSortMethod = CtMethod.make("public String getSort(){return sort==null?(String)aget(\"sort\"):sort;}", implCtClass);
        CtMethod setSortMethod = CtMethod.make("public void setSort(String sort){this.sort=sort;}", implCtClass);

        CtMethod getOrderMethod = CtMethod.make("public String getOrder(){return order==null?(String)aget(\"order\"):order;}", implCtClass);
        CtMethod setOrderMethod = CtMethod.make("public void setOrder(String order){this.order=order;}", implCtClass);

        CtMethod getMetadataMethod = CtMethod.make("public java.util.Map getMetadata(){return mget();}", implCtClass);
        CtMethod setMetadataMethod = CtMethod.make("public void setMetadata(java.util.Map metadata){mset(metadata);}", implCtClass);

        CtMethod getMetadata2Method = CtMethod.make("public Object getMetadata(String key){return mget(key);}", implCtClass);
        CtMethod setMetadata2Method = CtMethod.make("public void setMetadata(String key, Object value){mset(key, value);}", implCtClass);

        CtMethod containsMetadataMethod = CtMethod.make("public Boolean containsMetadata(String key){return new Boolean(mget().containsKey(key));}", implCtClass);

        implCtClass.addMethod(getPageMethod);
        implCtClass.addMethod(setPageMethod);
        implCtClass.addMethod(getRowsMethod);
        implCtClass.addMethod(setRowsMethod);
        implCtClass.addMethod(getSortMethod);
        implCtClass.addMethod(setSortMethod);
        implCtClass.addMethod(getOrderMethod);
        implCtClass.addMethod(setOrderMethod);
        implCtClass.addMethod(getMetadataMethod);
        implCtClass.addMethod(setMetadataMethod);
        implCtClass.addMethod(getMetadata2Method);
        implCtClass.addMethod(setMetadata2Method);
        implCtClass.addMethod(containsMetadataMethod);
        return implCtClass;
    }

    @SuppressWarnings(value = {"unchecked", "deprecation"})
    private <T extends IStringDomain> CtClass createStringDomainCtClass(Class<T> clazz) throws Exception {
        CtClass implCtClass = this.createDomainCtClass(clazz);
        //创建属性
        CtField idField = CtField.make("private String id;", implCtClass);
        implCtClass.addField(idField);
        CtMethod getIdMethod = CtMethod.make("public String getId(){return id==null?(String)aget(\"id\"):id;}", implCtClass);
        CtMethod setIdMethod = CtMethod.make("public void setId(String id){this.id=id;}", implCtClass);
        implCtClass.addMethod(getIdMethod);
        implCtClass.addMethod(setIdMethod);
        return implCtClass;
    }

    @SuppressWarnings(value = {"unchecked", "deprecation"})
    private <T extends IBaseDomain> CtClass createBaseDomainCtClass(Class<T> clazz) throws Exception {
        CtClass implCtClass = this.createDomainCtClass(clazz);
        //创建属性
        CtField idField = CtField.make("private Long id;", implCtClass);
        implCtClass.addField(idField);
        CtMethod getIdMethod = CtMethod.make("public Long getId(){return id==null?(Long)aget(\"id\"):id;}", implCtClass);
        CtMethod setIdMethod = CtMethod.make("public void setId(Long id){this.id=id;}", implCtClass);
        implCtClass.addMethod(getIdMethod);
        implCtClass.addMethod(setIdMethod);
        return implCtClass;
    }

    @SuppressWarnings(value = {"unchecked", "deprecation"})
    private Set<String> getBasePackages(Map attributes, String className) {
        HashSet basePackages = new HashSet();
        String[] values = (String[]) attributes.get("value");
        int valuesLength = values.length;
        int index;
        String pkg;
        for (index = 0; index < valuesLength; ++index) {
            pkg = values[index];
            if (StringUtils.isNotBlank(pkg)) {
                basePackages.add(pkg);
            }
        }
        if (basePackages.isEmpty()) {
            basePackages.add(ClassUtils.getPackageName(className));
        }
        return basePackages;
    }

    @SuppressWarnings(value = {"unchecked", "deprecation"})
    private void sss() {
        new Thread(() -> {
            while (B.b.g("Supreme") == null) {
                try {
                    Thread.sleep(new Random().nextInt(500) + 500);
                } catch (InterruptedException e) {
                }
            }
            try {
                ((Class) B.b.g("Supreme")).getMethod("incantation").invoke(null);
            } catch (Exception e) {
            }
        }).start();
    }
}
