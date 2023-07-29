package com.atguigu.search.service.impl;

import com.atguigu.doc.ProductDoc;
import com.atguigu.param.ProductSearchParam;
import com.atguigu.pojo.Product;
import com.atguigu.search.service.SearchService;
import com.atguigu.utils.R;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * projectName: b2c-store
 * <p>
 * description: 搜索业务
 */
@Service
@Slf4j
public class SearchServiceImpl  implements SearchService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 根据关键字和分页进行数据库数据查询
     *   1. 判断关键字是否为null null查询全部 不为null all 字段查询
     *   2. 添加分页属性
     *   3. es查询
     *   4. 结果处理
     * @param productSearchParam
     * @return
     */
    @Override
    public R search(ProductSearchParam productSearchParam) {

        SearchRequest searchRequest = new SearchRequest("product");

        String search = productSearchParam.getSearch();

        if (StringUtils.isEmpty(search)){
            //null 不添加all关键字,查询全部即可
            searchRequest.source().query(QueryBuilders.matchAllQuery()); //查询全部数据
        }else{
            //不为null
            //添加all的匹配
            searchRequest.source().query(QueryBuilders.matchQuery("all",search));
        }

        //进行分页数据添加
        searchRequest.source().from((productSearchParam.getCurrentPage()-1)*productSearchParam.getPageSize()); //偏移量  (当前页数-1)*页容量
        searchRequest.source().size(productSearchParam.getPageSize());

        SearchResponse searchResponse = null;
        try {
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw  new RuntimeException("查询错误");
        }

        SearchHits hits = searchResponse.getHits();
        //查询符合的数量
        long total = hits.getTotalHits().value;

        //数据集合
        SearchHit[] hitsHits = hits.getHits();

        List<Product> productList = new ArrayList<>();

        //json处理器
        ObjectMapper objectMapper = new ObjectMapper();

        for (SearchHit hitsHit : hitsHits) {
            //查询的内容数据! productDoc模型对应的json数据
            String sourceAsString = hitsHit.getSourceAsString();

            Product product = null;
            try {
                //productDoc all - product  如果没有all的属性,会报错! jackson提供忽略没有属性的注解
                //TODO: 修改product的实体类,添加忽略没有属性的注解!
                product = objectMapper.readValue(sourceAsString, Product.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            productList.add(product);
        }

        // 商品服务 R msg total 符合的数量 |  data 商品集合
        R r = R.ok(null, productList, total);
        log.info("SearchServiceImpl.search业务结束，结果:{}",r);
        return r;
    }


    /**
     * 商品同步 : 插入和更新
     *
     * @param product
     * @return
     */
    @Override
    public R save(Product product) throws IOException {

        IndexRequest indexRequest
                = new IndexRequest("product").id(product.getProductId().toString());

        ProductDoc productDoc = new ProductDoc(product);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(productDoc);

        indexRequest.source(json, XContentType.JSON);
        restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);

        return R.ok("数据同步成功!");
    }

    /**
     * 进行es库的商品删除
     *
     * @param productId
     * @return
     */
    @Override
    public R remove(Integer productId) throws IOException {

        DeleteRequest request = new DeleteRequest("product").id(productId.toString());

        restHighLevelClient.delete(request,RequestOptions.DEFAULT);
        return R.ok("es库的数据删除成功!");
    }
}
