package com.dili.trace.excel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;

public class ProductModel extends BaseRowModel {
	@ExcelProperty(index = 0)
	private String id;
	@ExcelProperty(index = 1)
	private String name;
	@ExcelProperty(index = 2)
	private String pingying;
	@ExcelProperty(index = 3)
	private String pyInitals;
	@ExcelProperty(index = 4)
	private String status;
	@ExcelProperty(index = 5)
	private String parent;
	@ExcelProperty(index = 6)
	private String path;
	@ExcelProperty(index = 7)
	private String code;

	private ProductModel parentModel;
	private List<ProductModel> childrenModel = new ArrayList<>();

	public ProductModel getParentModel() {
		return parentModel;
	}

	public void setParentModel(ProductModel parentModel) {
		this.parentModel = parentModel;
	}

	public List<ProductModel> getChildrenModel() {
		return childrenModel;
	}

	public void setChildrenModel(List<ProductModel> childrenModel) {
		this.childrenModel = childrenModel;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getPingying() {
		return pingying;
	}

	public String getPyInitals() {
		return pyInitals;
	}

	public String getStatus() {
		return status;
	}

	public String getParent() {
		return parent;
	}

	public String getPath() {
		return path;
	}

	public String getCode() {
		return code;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPingying(String pingying) {
		this.pingying = pingying;
	}

	public void setPyInitals(String pyInitals) {
		this.pyInitals = pyInitals;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "ProductModel [id=" + id + ", name=" + name + ", pingying=" + pingying + ", pyInitals=" + pyInitals
				+ ", status=" + status + ", parent=" + parent + ", path=" + path + ", code=" + code + "]";
	}

	public static List<ProductModel> parseExcel(InputStream is) {

		List<ProductModel> resultList = new ArrayList<>();
		AnalysisEventListener<ProductModel> excelListener = new AnalysisEventListener<ProductModel>() {

			@Override
			public void doAfterAllAnalysed(AnalysisContext ctx) {
			}

			@Override
			public void invoke(ProductModel model, AnalysisContext ctx) {
				Stream.of(model).filter(Objects::nonNull).collect(Collectors.toCollection(() -> resultList));
			}

		};

		EasyExcelFactory.readBySax(is, new Sheet(1, 1, ProductModel.class), excelListener);
		return resultList;
	}

	public static List<ProductModel> buildTree(List<ProductModel> modelList) {

		if (CollectionUtils.isEmpty(modelList)) {
			return new ArrayList<>();
		} else {
			Map<String, ProductModel> idModelMap = modelList.stream()
					.collect(Collectors.toMap(ProductModel::getId, Function.identity()));
			List<ProductModel> treeList = idModelMap.values().stream().filter(Objects::nonNull).map(item -> {
				ProductModel parentModel = idModelMap.getOrDefault(item.getParent(), null);
				if (parentModel != null) {
					item.setParentModel(parentModel);
					parentModel.getChildrenModel().add(item);
				}
				return item;

			}).filter(item -> "0".equals(item.getParent())).collect(Collectors.toList());

			return treeList;
		}

	}

	public static List<ProductModel> rebuildId(ProductModel parent,List<ProductModel> childList, AtomicLong startId) {
		childList.stream().forEach(item -> {
			item.setId(String.valueOf(startId.getAndIncrement()));
			if(parent==null) {
				item.setParent("0");
				item.setPath(item.getId()+",");
			}else {
				item.setParent(parent.getId());
				item.setPath(parent.getPath()+item.getId()+",");
			}
//			System.out.println(item);
			rebuildId(item,item.getChildrenModel(), startId);
			
		});

		return childList;
	}
	public static List<ProductModel> treeToList(List<ProductModel> childList) {
		
		return childList.stream().flatMap(item->{
			return Stream.concat(Stream.of(item), treeToList(item.getChildrenModel()).stream());
		}).collect(Collectors.toList());
	}

	public static void main(String[] args) throws IOException {
		try (FileInputStream fs = new FileInputStream("d:/寿光地利老系统商品信息（7.31）.xlsx");) {

			List<ProductModel> list = ProductModel.parseExcel(fs);
			//list.stream().forEach(System.out::println);
			List<ProductModel> rootList = ProductModel.buildTree(list);
			List<ProductModel> treeList=ProductModel.rebuildId(null,rootList, new AtomicLong(5));
			treeToList(treeList).stream().forEach(System.out::println);
		}

	}
}
