package com.atguigu.gmall.search.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.atguigu.gmall.annotations.LoginRequired;
import com.atguigu.gmall.bean.PmsBaseAttrInfo;
import com.atguigu.gmall.bean.PmsBaseCatalog1;
import com.atguigu.gmall.bean.PmsSearchSkuInfo;
import com.atguigu.gmall.bean.PmsSkuAttrValue;
import com.atguigu.gmall.bean.PmsSkuInfo;
import com.atguigu.gmall.service.AttrService;
import com.atguigu.gmall.service.CatalogService;
import com.atguigu.gmall.service.ItemSearchService;
import com.atguigu.gmall.util.Constants;
import com.atguigu.gmall.util.CookieUtil;
import com.atguigu.gmall.util.CreateFileJson;

import io.searchbox.action.Action;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

@Controller
@CrossOrigin // 解决跨域问题
public class ItemSearchController {

	@Autowired
	JestClient jestClient;
	
	
	@Reference
	private ItemSearchService itemSearchService;
	
	@Reference
	private AttrService attrService;
	
	
	@RequestMapping("/index")
	@LoginRequired(loginSuccess=false)	//比如登陆成功 直接登录的 挑这个也念 也保存了cookie
	public String index(HttpServletRequest request) {
		request.getAttribute("memberId");
		
		return "index";
	}

	
	@Reference
	private CatalogService catalogService;
	
	@ResponseBody
	@RequestMapping("/cats")
	public List<PmsBaseCatalog1> queryPmsBaseCata1(){
		//查询所有分类 
		List<PmsBaseCatalog1> list=catalogService.queryAllCat();
		String dString=JSON.toJSONString(list);
		System.out.println(dString);
		//写入生成
		CreateFileJson.createJsonFile(list, "E://test//a.json");
		return list;
	}
	/*
	 * 查询数据首页 名字 分类id
	 */
	@RequestMapping("list.html")
	public String list(String catalog3Id,String keyword,ModelMap map) throws IOException {
		
		//查询
		SearchSourceBuilder sourceBuilder=new SearchSourceBuilder();
		//条件
		BoolQueryBuilder boolQueryBuilder=new BoolQueryBuilder();
		if (StringUtils.isNotBlank(catalog3Id)) {
			TermQueryBuilder termQueryBuilder=new TermQueryBuilder("catalog3Id", catalog3Id);
			boolQueryBuilder.filter(termQueryBuilder);
		}
		if (StringUtils.isNotBlank(keyword)) {
			MatchQueryBuilder matchQueryBuilder=new MatchQueryBuilder("skuName", keyword);
			boolQueryBuilder.must(matchQueryBuilder);
		}
		
		   sourceBuilder.query(boolQueryBuilder);
		   HighlightBuilder highlightBuilder = new HighlightBuilder();
		   highlightBuilder.preTags("<span style='color:red;'>");
		   highlightBuilder.field("skuName"); 
		   highlightBuilder.postTags("</span>");
		  
		   sourceBuilder.highlight(highlightBuilder); // sort
		   sourceBuilder.sort("id",SortOrder.DESC);
		   
			//分页显示
		sourceBuilder.from(0);
		sourceBuilder.size(20);
		sourceBuilder.highlight(null);//条件显示高亮
		String jsonString=sourceBuilder.toString();
		List<PmsSearchSkuInfo> pmsSearchSkuInfos = new ArrayList<>();
		Search search=new Search.Builder(jsonString).addIndex("gmall").addType("PmsSkuInfo").build();
		SearchResult execute=jestClient.execute(search);
		List<SearchResult.Hit<PmsSearchSkuInfo, Void>> hits=execute.getHits(PmsSearchSkuInfo.class);
		for (SearchResult.Hit<PmsSearchSkuInfo, Void> hit : hits) {
			PmsSearchSkuInfo info=hit.source;
		Map<String,List<String>> higMap=hit.highlight;
		//高亮名字查询才有
		if (higMap!=null) {
			info.setSkuName(higMap.get("skuName").get(0)+"");	
		}
		
			pmsSearchSkuInfos.add(info);
		}
		Set<String> vals=new HashSet<String>();
		//拿到查询到的商品的每个平台属性 去重复查询出关联的属性 
		for (PmsSearchSkuInfo pms : pmsSearchSkuInfos) {
			List<PmsSkuAttrValue> pmsSkuAttrValueList=pms.getSkuAttrValueList();
			for (PmsSkuAttrValue pmsSkuAttrValue : pmsSkuAttrValueList) {
				vals.add(pmsSkuAttrValue.getAttrId());
				
			}
		}
		//查询关联的平台属性
		List<PmsBaseAttrInfo> pmsBaseAttrInfoList=attrService.queryAttrSku(vals);
		map.put("attrList", pmsBaseAttrInfoList);
		map.put("skuLsInfoList", pmsSearchSkuInfos);
		map.put("catalog3Id", catalog3Id);
		map.put("keyword", keyword);
		return "list";
	}
	


	
	@RequestMapping("skuAllSerarch")
	@ResponseBody
	public List<PmsSearchSkuInfo> skuAllSerarch() throws IllegalAccessException, InvocationTargetException {
		
		List<PmsSkuInfo> pmsSkuInfoList=itemSearchService.queryAllSearchSku();
	
		List<PmsSearchSkuInfo> pmsSearchSkuInfoList=new ArrayList<PmsSearchSkuInfo>();
		for (PmsSkuInfo pmsSkuInfo : pmsSkuInfoList) {
			PmsSearchSkuInfo searchSkuInfo=new PmsSearchSkuInfo();
			//转换对象 集合有bug
			BeanUtils.copyProperties(pmsSkuInfo, searchSkuInfo);;
			/*
			 * searchSkuInfo.setCatalog3Id(pmsSkuInfo.getCatalog3Id());
			 * searchSkuInfo.setPrice(pmsSkuInfo.getPrice());
			 * searchSkuInfo.setProductId(pmsSkuInfo.getProductId());
			 * searchSkuInfo.setSkuDesc(pmsSkuInfo.getSkuDesc());
			 * searchSkuInfo.setSkuDefaultImg(pmsSkuInfo.getSkuDefaultImg());
			 * searchSkuInfo.setSkuAttrValueList(pmsSkuInfo.getSkuAttrValueList());
			 * searchSkuInfo.setSkuName(pmsSkuInfo.getSkuName());
			 */
			searchSkuInfo.setId(Long.parseLong(pmsSkuInfo.getId()));
			
			//searchSkuInfo.setSkuAttrValueList(pmsSkuInfo.getSkuAttrValueList());
		//	System.out.println(pmsSkuInfo.getSkuAttrValueList());
			pmsSearchSkuInfoList.add(searchSkuInfo);
		}
		
		System.out.println(pmsSearchSkuInfoList.size());
		for (PmsSearchSkuInfo pmsSearchSkuInfo : pmsSearchSkuInfoList) {
			Index put = new Index.Builder(pmsSearchSkuInfo).index("gmall").type("PmsSkuInfo").id(pmsSearchSkuInfo.getId()+"").build();
			try {
				jestClient.execute(put);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return pmsSearchSkuInfoList;

	}
	
	@RequestMapping("/jestTest")
	@ResponseBody
	public List<PmsSearchSkuInfo> jestTest() throws IOException {
		//jest的dsl的工具
		SearchSourceBuilder sourceBuilder=new SearchSourceBuilder();
		BoolQueryBuilder boolQueryBuilder=new BoolQueryBuilder();
	
		//TermQueryBuilder termQueryBuilder = new TermQueryBuilder("skuAttrValueList.valueId","43");
		//boolQueryBuilder.filter(termQueryBuilder);
		
		MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("skuName","华为");
		boolQueryBuilder.must(matchQueryBuilder);
		 
		sourceBuilder.query(boolQueryBuilder);
		
		//分页
		sourceBuilder.from(0);
		sourceBuilder.size(200);
		sourceBuilder.highlight(null);//条件显示高亮
		String jsonString=sourceBuilder.toString();
		System.out.println(jsonString);
		//api执行查询
		List<PmsSearchSkuInfo> pmsSearchSkuInfos = new ArrayList<>();
		Search search=new Search.Builder(jsonString).addIndex("gmall").addType("PmsSkuInfo").build();
		SearchResult execute=jestClient.execute(search);
		List<SearchResult.Hit<PmsSearchSkuInfo, Void>> hits=execute.getHits(PmsSearchSkuInfo.class);
		for (SearchResult.Hit<PmsSearchSkuInfo, Void> hit : hits) {
			PmsSearchSkuInfo info=hit.source;
		
	         
			pmsSearchSkuInfos.add(info);
		}
		return pmsSearchSkuInfos;
	}
}
