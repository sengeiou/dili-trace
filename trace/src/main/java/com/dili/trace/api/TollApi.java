package com.dili.trace.api;

/**
 * Created by laikui on 2019/7/26.
 */
//@RestController
//@RequestMapping(value = "/api/toll")
//@Api(value ="/api/toll", description = "对接神农基础信息相关接口")
//@InterceptConfiguration
public class TollApi {
//    private static final Logger LOGGER = LoggerFactory.getLogger(TollApi.class);
//    @Autowired
//    BaseInfoRpc baseInfoRpc;
//
//
//    @RequestMapping("/category")
//    @ResponseBody
//    public Map<String, ?> listByName(String name, boolean allFlag) {
//        List<Category> categorys = queryCategorys(name);
//
//        List<Map<String, Object>> list = Lists.newArrayList();
//        if (categorys != null && categorys.size() > 0) {
//            for (Category c : categorys) {
//                /*String[] split = c.getPath().split(",");
//                Long parentId = c.getParent();
//                String parentName = c.getName();*/
//                Map<String, Object> obj = Maps.newHashMap();
//                obj.put("id", c.getId());
//                obj.put("data", name);
//                obj.put("value", c.getName());
//                list.add(obj);
//            }
//        }
//        Map map = Maps.newHashMap();
//        map.put("suggestions", list);
//        return map;
//    }
//    @RequestMapping(value="/city",method=RequestMethod.GET)
//    @ResponseBody
//    public Map<String, ?> queryCity(String name) {
//        List<Map<String, Object>> list = Lists.newArrayList();
//        if(StringUtils.isNotBlank(name)){
//            try{
//                List<City> citys =queryCitys(name);
//                for (City city : citys) {
//                    Map<String, Object> obj = Maps.newHashMap();
//                    obj.put("id", city.getId());
//                    obj.put("data", name);
//                    obj.put("name", city.getName());
//                    obj.put("value", city.getMergerName());
//                    obj.put("customCode", city.getCustomCode());
//                    list.add(obj);
//                }
//            } catch (Exception e) {
//
//            }
//        }
//
//        Map map = Maps.newHashMap();
//        map.put("suggestions", list);
//        return map;
//    }
//
//    private List<Category> queryCategorys(String name) {
//        CategoryListInput query = new CategoryListInput();
//        query.setKeyword(name);
//        BaseOutput<List<Category>> result = baseInfoRpc.listCategoryByCondition(query);
//        if(result.isSuccess()){
//            return result.getData();
//        }
//        List<Category> citys = new ArrayList<>();
//        /*Category city = new Category();
//        city.setName("苹果");
//        city.setId(1L);
//        city.setParent(0L);
//        citys.add(city);
//        Category city1 = new Category();
//        city1.setName("苹果2");
//        city1.setId(2L);
//        city1.setParent(1L);
//        citys.add(city1);*/
//        return citys;
//    }
//    private List<City> queryCitys(String name) {
//        CityListInput query = new CityListInput();
//        query.setKeyword(name);
//        BaseOutput<List<City>> result = baseInfoRpc.listCityByCondition(query);
//        if(result.isSuccess()){
//            return result.getData();
//        }
//        List<City> citys = new ArrayList<>();
//        /*City city = new City();
//        city.setName("成都");
//        city.setMergerName("四川成都");
//        city.setId(1L);
//        city.setParentId(0L);
//        citys.add(city);
//        City city1 = new City();
//        city1.setName("成南");
//        city1.setMergerName("四川成南");
//        city1.setId(2L);
//        city1.setParentId(1L);
//        citys.add(city1);*/
//        return citys;
//    }

}
