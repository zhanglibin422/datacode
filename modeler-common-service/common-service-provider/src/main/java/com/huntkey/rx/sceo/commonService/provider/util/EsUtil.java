package com.huntkey.rx.sceo.commonService.provider.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huntkey.rx.sceo.commonService.provider.model.Criteria;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest.AliasActions;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsRequest;
import org.elasticsearch.action.bulk.*;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.percentiles.hdr.InternalHDRPercentiles;
import org.elasticsearch.search.sort.*;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.SortOrder;
import java.io.IOException;
import java.util.*;

/**
 * ES处理的模板类
 *
 * @ClassName:
 * @Description:
 */
@Component
public class EsUtil {

    private org.elasticsearch.search.sort.SortOrder Sor;
    private AggregationBuilder aggregationBuilder;

    /**
     * 构建ES索引
     *
     * @param client
     * @param indexName
     * @return
     */
    public boolean createIndex(Client client, String indexName) {
        return client.admin().indices().create(Requests.createIndexRequest(indexName))
                .actionGet().isAcknowledged();
    }

    /**
     * 构建索引同义词
     *
     * @param client
     * @param index
     * @param alias_index
     * @return
     */
    public boolean createAlias(Client client, String index, String alias_index) throws Exception {

        IndicesAliasesRequest request = new IndicesAliasesRequest();
        request.addAliasAction(AliasActions.add().alias(alias_index).index(index));
        IndicesAliasesResponse response = client.admin().indices().aliases(request).get();

        return response.isAcknowledged();
    }

    /**
     * 删除索引同义词
     *
     * @param client
     * @param index
     * @param alias_index
     * @return
     */
    public boolean removeAlias(Client client, String index, String alias_index) throws Exception {

        IndicesAliasesRequest request = new IndicesAliasesRequest();
        request.addAliasAction(AliasActions.remove().alias(alias_index).index(index));
        IndicesAliasesResponse response = client.admin().indices().aliases(request).get();

        return response.isAcknowledged();
    }

    /**
     * 删除指定的索引
     *
     * @param indexName
     * @return boolean
     */
    public boolean deleteIndex(Client client, String indexName) {
        if (indexExists(client, indexName)) {
            return client.admin().indices().prepareDelete(indexName).execute().actionGet().isAcknowledged();
        }
        return false;
    }

    /**
     * 指定的索引的名称是否存在
     *
     * @param indexName
     * @return boolean
     */
    public boolean indexExists(Client client, String indexName) {
        return client.admin().indices().exists(Requests.indicesExistsRequest(indexName)).actionGet().isExists();
    }

    /**
     * 构建索引  根据索引的名称和设置信息
     *
     * @param indexName
     * @param settings
     * @return boolean
     */
    public boolean createIndex(Client client, String indexName, Object settings) {
        CreateIndexRequestBuilder requestBuilder = client.admin().indices().prepareCreate(indexName);
        if (settings != null) {
            if (settings instanceof String) {
                requestBuilder.setSettings(String.valueOf(settings),XContentType.JSON);
            } else if (settings instanceof Map) {
                requestBuilder.setSettings((Map) settings);
            } else if (settings instanceof XContentBuilder) {
                requestBuilder.setSettings((XContentBuilder) settings);
            }
        }
        return requestBuilder.execute().actionGet().isAcknowledged();
    }

    /**
     * 使用prepareMapping形式进行创建mapping
     *
     * @param indexName
     * @param type
     * @param mappings
     * @return
     * @throws IOException
     */
    public boolean putMapping(Client client, String indexName, String type, Map<String, Map<String, String>> mappings) throws IOException {
        XContentBuilder mappingSource = XContentFactory.jsonBuilder()
                .startObject().startObject(type)
                //.startObject("_all").field("analyzer", "ik_max_word")
                //.field("search_analyzer", "ik_max_word").field("term_vector", "no").endObject()
                //----初始
                .startObject("properties");

        Map<String, String> currentMap = null;
        for (Map.Entry<String, Map<String, String>> entry : mappings.entrySet()) {
            currentMap = entry.getValue();
            mappingSource.startObject(entry.getKey());
            for (Map.Entry<String, String> secondEntry : currentMap.entrySet()) {
                mappingSource.field(secondEntry.getKey(), secondEntry.getValue());
            }
            mappingSource.endObject();
        }

        mappingSource.endObject().endObject().endObject();

        boolean res = client.admin().indices().preparePutMapping(indexName).setType(type)
                .setSource(mappingSource).execute().actionGet().isAcknowledged();

        return res;
    }

    /**
     * 获取mapping
     *
     * @param indexName
     * @param type
     * @return Map
     */
    public Map<String, Object> getMapping(Client client, String indexName, String type) {
        Map<String, Object> mappings = null;
        try {
            mappings = client.admin().indices().getMappings(new GetMappingsRequest().indices(indexName).types(type))
                    .actionGet().getMappings().get(indexName).get(type).getSourceAsMap();
        } catch (Exception e) {
            throw new ElasticsearchException("Error while getting mapping for indexName : " + indexName + " type : " + type + " " + e.getMessage());
        }
        return mappings;
    }

    /**
     * 判断mapping是否存在
     *
     * @param client
     * @param indexName
     * @param type
     * @return
     */
    public boolean mappingExists(Client client, String indexName, String type) {
        return client.admin().cluster().prepareState().execute()
                .actionGet().getState().metaData().index(indexName)
                .getMappings().containsKey(type);
    }

    /**
     * 创建一个文档
     * @param index index
     * @param type type
     * @param jsonObject
     */
    public void createDoc(Client client,String index, String type, String id, JSONObject jsonObject) {
        try {
            //IndexResponse indexResponse =
                    client.prepareIndex()
                    .setIndex(index)
                    .setType(type)
                    .setId(id) // 如果没有设置id，则ES会自动生成一个id
                    .setSource(jsonObject.toJSONString(),XContentType.JSON)
                    .get();
        } catch (ElasticsearchException e) {
            e.printStackTrace();
        }
    }

    /**
     * 小数据量的批量一次提交
     * 批量添加索引数据 ，这里keyId可以允许为空，为空则使用ES为我们默认生成的id，
     * keyId不为空，那么该字段为后面map数据中某个key，然后程序会根据该key获取的值作为id进行加入ES
     *
     * @param indexName
     * @param type
     * @param keyId     @Nullable Object keyId,boolean keyVal2Column,
     */
    public void bulkIndex(Client client, String indexName, String type, String keyId, List<Map<String, Object>> listMap) {
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        IndexRequestBuilder indexRequest = null;
        String id = "";
        if (StringUtils.isNotBlank(keyId)) {
            for (Map<String, ?> currentMap : listMap) {
                id = (String) currentMap.get(keyId);

                Map<String, ?> tempMap = currentMap;
                if(currentMap.containsKey(keyId)) {
                    tempMap.remove(keyId);
                }
                indexRequest = client.prepareIndex(indexName, type, id);
                indexRequest.setSource(JSONObject.toJSONString(tempMap), XContentType.JSON);
                bulkRequest.add(indexRequest);
            }
        } else {
            for (Map<String, ?> currentMap : listMap) {
                indexRequest = client.prepareIndex(indexName, type);
                indexRequest.setSource(currentMap);
                bulkRequest.add(indexRequest);
            }
        }

        BulkResponse bulkResponse = bulkRequest.get();
        if (bulkResponse.hasFailures()) {
            // process failures by iterating through each bulk response item
            Map<String, String> failedDocuments = new HashMap<String, String>();
            for (BulkItemResponse item : bulkResponse.getItems()) {
                if (item.isFailed())
                    failedDocuments.put(item.getId(), item.getFailureMessage());
            }
            throw new ElasticsearchException(
                    "Bulk indexing has failures. Use ElasticsearchException.getFailedDocuments() for detailed messages ["
                            + failedDocuments + "]", failedDocuments
            );
        }
    }

    /**
     * 细粒度的批量提交   BackoffPolicy 批量请求重试失败
     * 有失败会抛出异常     throw new ElasticsearchException(failure);
     *
     * @param indexName
     * @param type
     * @param bulkSize           单位为MB，批量提交总大小，设置-1可禁用
     * @param bulkActions        提交的批次量，默认是1000，设置-1可以禁用
     * @param flushInterval      提交时间间隔
     * @param concurrentRequests 线程数
     * @param listMap
     */
    public void bulkProcessorIndex(Client client, String indexName, String type, String keyId, long bulkSize, int bulkActions,
                                   long flushInterval, int concurrentRequests, List<Map<String, Object>> listMap) {
        IndexRequest indexRequest = null;
        BulkProcessor bulkProcessor = BulkProcessor.builder(client, new BulkProcessor.Listener() {
            public void beforeBulk(long executionId, BulkRequest request) {
                // TODO Auto-generated method stub
            }

            public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
                // TODO Auto-generated method stub
            }

            public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
                // TODO Auto-generated method stub
                throw new ElasticsearchException(failure);
            }

        }).setBulkActions(bulkActions).setBulkSize(new ByteSizeValue(bulkSize, ByteSizeUnit.MB))
                .setFlushInterval(TimeValue.timeValueSeconds(flushInterval))
                .setConcurrentRequests(concurrentRequests)
                .setBackoffPolicy(
                        BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(500), 3))
                .build();
        if (StringUtils.isNotBlank(keyId)) {
            for (Map<String, ?> currentMap : listMap) {
                indexRequest = new IndexRequest(indexName, type, currentMap.get(keyId).toString());
                indexRequest.source(JSONObject.toJSONString(currentMap),XContentType.JSON);
                //System.out.println(JSONObject.toJSONString(currentMap));
                bulkProcessor.add(indexRequest);
            }
        } else {
            for (Map<String, ?> currentMap : listMap) {
                indexRequest = new IndexRequest(indexName, type);
                indexRequest.source(JSONObject.toJSONString(currentMap),XContentType.JSON);
                bulkProcessor.add(indexRequest);
            }
        }

        bulkProcessor.flush();//收尾刷新一次
        bulkProcessor.close();
    }

    /**
     * 细粒度的批量更新   BackoffPolicy 批量请求重试失败
     * 有失败会抛出异常     throw new ElasticsearchException(failure);
     *
     * @param indexName
     * @param type
     * @param bulkSize           单位为MB，批量提交总大小，设置-1可禁用
     * @param bulkActions        提交的批次量，默认是1000，设置-1可以禁用
     * @param flushInterval      提交时间间隔
     * @param concurrentRequests 线程数
     * @param listMap
     */
    public void bulkProcessorUpdate(Client client, String indexName, String type, String keyId, long bulkSize, int bulkActions,
                                    long flushInterval, int concurrentRequests, List<Map<String, Object>> listMap) {
        if (StringUtils.isBlank(keyId)) {
            return;
        }
        UpdateRequest updateRequest = null;
        BulkProcessor bulkProcessor = BulkProcessor.builder(client, new BulkProcessor.Listener() {
            public void beforeBulk(long executionId, BulkRequest request) {
                // TODO Auto-generated method stub
            }

            public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
                // TODO Auto-generated method stub
            }

            public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
                // TODO Auto-generated method stub
                throw new ElasticsearchException(failure);
            }

        }).setBulkActions(bulkActions).setBulkSize(new ByteSizeValue(bulkSize, ByteSizeUnit.MB))
                .setFlushInterval(TimeValue.timeValueSeconds(flushInterval))
                .setConcurrentRequests(concurrentRequests)
                .setBackoffPolicy(
                        BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(500), 3))
                .build();
        if (StringUtils.isNotBlank(keyId)) {
            for (Map<String, ?> currentMap : listMap) {
                updateRequest = new UpdateRequest(indexName, type, currentMap.get(keyId).toString());
                updateRequest.doc(JSONObject.toJSONString(currentMap),XContentType.JSON);
                //System.out.println(JSONObject.toJSONString(currentMap));
                bulkProcessor.add(updateRequest);
            }
        }

        bulkProcessor.flush();//收尾刷新一次
        bulkProcessor.close();
    }

    /**
     * 细粒度的批量删除
     * 有失败会抛出异常     throw new ElasticsearchException(failure);
     *
     * @param indexName
     * @param type
     * @param bulkSize           单位为MB，批量提交总大小，设置-1可禁用
     * @param bulkActions        提交的批次量，默认是1000，设置-1可以禁用
     * @param flushInterval      提交时间间隔
     * @param concurrentRequests 线程数
     * @param ids                要删除的id集合
     */
    public void bulkDelete(Client client, String indexName, String type, long bulkSize, int bulkActions,
                           long flushInterval, int concurrentRequests, List<Object> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        DeleteRequest deleteRequest = null;
        BulkProcessor bulkProcessor = BulkProcessor.builder(client, new BulkProcessor.Listener() {
            public void beforeBulk(long executionId, BulkRequest request) {
                // TODO Auto-generated method stub
            }

            public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
                // TODO Auto-generated method stub
            }

            public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
                // TODO Auto-generated method stub
                throw new ElasticsearchException(failure);
            }

        }).setBulkActions(bulkActions).setBulkSize(new ByteSizeValue(bulkSize, ByteSizeUnit.MB))
                .setFlushInterval(TimeValue.timeValueSeconds(flushInterval))
                .setConcurrentRequests(concurrentRequests)
                .setBackoffPolicy(
                        BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(500), 3))
                .build();

        for (Object id : ids) {
            deleteRequest = new DeleteRequest(indexName, type, String.valueOf(id));
            bulkProcessor.add(deleteRequest);
        }

        bulkProcessor.flush();//收尾刷新一次
        bulkProcessor.close();
    }

    /**
     * 根据传入的index，type和批量的id来进行获取相应的数据
     *
     * @param index
     * @param type
     * @param ids
     * @return
     */
    public List<Map<String, Object>> queryMultiGet(Client client, String index, String type, String... ids) {
        List<Map<String, Object>> listData = null;
        GetResponse response = null;
        MultiGetResponse multiGetResponse = client.prepareMultiGet()
                .add(index, type, ids).get();
        listData = new ArrayList<Map<String, Object>>();
        for (MultiGetItemResponse itemResponse : multiGetResponse) {
            response = itemResponse.getResponse();
            if (response.isExists()) {
                listData.add(response.getSourceAsMap());
            }
        }
        return listData;
    }

    /**
     * 多条件查询
     *
     * @param indexName
     * @param type
     * @param field
     * @param fieldNames
     */
    public void queryMultiMatch(Client client, String indexName, String type, String field, String... fieldNames) {
        QueryBuilder qb = QueryBuilders.multiMatchQuery(field, fieldNames);
        SearchRequestBuilder srb = client.prepareSearch();
        srb.setIndices(indexName).setTypes(type).setQuery(qb);
        SearchResponse searchResponse = srb.execute().actionGet();
        SearchHits searchHits = searchResponse.getHits();
        for (int i = 0; i < searchHits.getHits().length; i++) {
            System.out.println(searchHits.getHits()[i].getSourceAsString());
        }
    }

    /**
     *  同一关键字多条件查询
     *
     * @param indexName
     * @param type
     * @param field
     * @param fieldNames
     */
    public List<String> matchPhrase(Client client, String indexName, String type, String fieldNames, String field) {
        List<String> listData = null;
        QueryBuilder qb = QueryBuilders.matchPhrasePrefixQuery(field, fieldNames);
        SearchRequestBuilder srb = client.prepareSearch();
        srb.setIndices(indexName).setTypes(type).setQuery(qb).setFrom(0).setSize(200);
        SearchResponse searchResponse = srb.execute().actionGet();
        SearchHits searchHits = searchResponse.getHits();

        listData = new ArrayList<String>();
        for (int i = 0; i < searchHits.getHits().length; i++) {
            listData.add(searchHits.getHits()[i].getId());
        }

        return listData;
    }

    /**
     * 多条件组合查询  and关系
     * @param client
     * @param indexName
     * @param entries
     * @param pageNum
     * @param pageSize
     * @return
     */
    public List<String> matchMorePhrase(Client client, String indexName,Iterator entries, int pageNum, int pageSize) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        String name = "";
        String value = "";
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();

            BoolQueryBuilder qb = QueryBuilders.boolQuery();
            name = entry.getKey().toString();
            value = entry.getValue().toString();
            qb.should(QueryBuilders.matchPhrasePrefixQuery(name, value));

            boolQueryBuilder.must(qb);
        }

        List<String> listData = null;
        SearchRequestBuilder srb = client.prepareSearch();
        srb.setIndices(indexName).setTypes(indexName).setQuery(boolQueryBuilder).setFrom(pageNum).setSize(pageSize);
        SearchResponse searchResponse = srb.execute().actionGet();
        SearchHits searchHits = searchResponse.getHits();

        listData = new ArrayList<String>();
        for (int i = 0; i < searchHits.getHits().length; i++) {
            listData.add(searchHits.getHits()[i].getId());
        }

        return listData;
    }

    /**
     * 多条件组合查询  and关系
     * @param client
     * @param indexName
     * @param criteria
     * @return
     */
    public List<String> matchMorePhrase(Client client, String indexName, Criteria criteria) {
     //  getSort(client, indexName, indexName ,criteria); //排序测试函数
        JSONArray conditions = criteria.getConditions();        //  条件
        JSONObject pagenation = criteria.getPagenation();       // 分页信息
        JSONArray orderBys = criteria.getOrderBy();             // 排序信息

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        Iterator<Object> entries = conditions.iterator();
        JSONObject json = null;		// 数据行

        String attr = "";       // 字段名称
        String value = "";      // 字段值
        String operator = "";   // 操作符
        List<String> listData = null;
        //es 初始化,设置index 和 type
        SearchRequestBuilder srb = client.prepareSearch().setIndices(indexName).setTypes(indexName);
        if (pagenation != null && !pagenation.isEmpty()){
           srb = setPagenation(srb,pagenation);
        }
        //条件设置
        //设置where 查询条件
        if (conditions.size() > 0 ){
            //进行数据过滤组装
            while (entries.hasNext()) {
                json = (JSONObject)entries.next();
                attr = json.getString("attr");
                value = json.getString("value");
                operator = json.getString("operator");
                BoolQueryBuilder qb = QueryBuilders.boolQuery();

                if ("=".equals(operator)) {
                    qb.should(QueryBuilders.termQuery(attr, value));//完全匹配(字符)
                    //  非完全匹配
                    //   qb.should(QueryBuilders.matchQuery(attr,value));
                    //     qb.should(QueryBuilders.matchPhraseQuery(attr,value));
                }else if ("like".equals(operator)){
                    qb.should(QueryBuilders.matchPhrasePrefixQuery(attr, value));
                }
                boolQueryBuilder.must(qb);
            }
            srb.setQuery(boolQueryBuilder);
        }
        SearchResponse searchResponse = srb.execute().actionGet();
        SearchHits searchHits = searchResponse.getHits();

        listData = new ArrayList<String>();
        int len = searchHits.getHits().length;
        for (int i = 0; i < len; i++) {
            String rowkey = searchHits.getHits()[i].getId();
            listData.add(rowkey);
            System.out.println("rowkey:"+rowkey);
        }
        return listData;
    }

    /**
     * 返回总记录数  and关系
     * @param client
     * @param indexName
     * @param criteria
     * @return
     */
    public Long getTotalSize(Client client, String indexName, Criteria criteria) {
        JSONArray conditions = criteria.getConditions();        //  条件
        JSONObject pagenation = criteria.getPagenation();       // 分页信息

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        Iterator<Object> entries = conditions.iterator();
        JSONObject json = null;		// 数据行

        String attr = "";       // 字段名称
        String value = "";      // 字段值
        String operator = "";   // 操作符

        while (entries.hasNext()) {
            json = (JSONObject)entries.next();
            attr = json.getString("attr");
            value = json.getString("value");
            operator = json.getString("operator");

            BoolQueryBuilder qb = QueryBuilders.boolQuery();

            if ("=".equals(operator)) {
                //qb.should(QueryBuilders.termQuery(attr, value));
                qb.should(QueryBuilders.matchQuery(attr,value));
            }else if ("like".equals(operator)){
                qb.should(QueryBuilders.matchPhrasePrefixQuery(attr, value));
            }
            boolQueryBuilder.must(qb);
        }

        List<String> listData = null;
        SearchRequestBuilder srb = client.prepareSearch();
        srb.setIndices(indexName).setTypes(indexName).setQuery(boolQueryBuilder);

        SearchResponse searchResponse = srb.execute().actionGet();
        SearchHits searchHits = searchResponse.getHits();

        return searchHits.getTotalHits();
    }

    /**
     * 过滤字符串,callFields可为null，为null返回全部字段，否则返回指定的字段
     *
     * @param indexName
     * @param type
     * @param filterMap
     * @param callFields
     * @return List<Map>
     */
    public List<Map> queryStringQuery(Client client, String indexName, String type, Map<String, Object> filterMap,
                                      int from, int limit, String... callFields) {
        SearchRequestBuilder requestBuilder = client.prepareSearch(indexName);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        QueryStringQueryBuilder queryString = null;
        MatchAllQueryBuilder matchAll = null;
        if (filterMap != null && !filterMap.isEmpty()) {
            for (Map.Entry entry : filterMap.entrySet()) {
                queryString = QueryBuilders.queryStringQuery(String.valueOf(entry.getValue()));
                queryString.field(String.valueOf(entry.getKey()));
                boolQueryBuilder.must(queryString);
            }
        } else {//检索全部
            matchAll = QueryBuilders.matchAllQuery();
            boolQueryBuilder.must(matchAll);
        }

        requestBuilder.setTypes(type).setQuery(boolQueryBuilder);
        requestBuilder.setFrom(from).setSize(limit);

        requestBuilder.setFetchSource(callFields, null);    //显示指定字段的内容

        SearchResponse searchResponse = requestBuilder.execute().actionGet();

        return result2MapData(searchResponse);
    }

    /**
     * 该方法是response中的数据进行处理成List<Map>数据进行返回
     *
     * @param searchResponse
     * @return List<Map>
     * queryStringQuery  不能使用searchHit.getSourceAsString()来取值，只能使用迭代遍历的方式
     */
    public List<Map> result2MapData(SearchResponse searchResponse) {
        List<Map> listData = new ArrayList<Map>();
        //Map currentMap = null;
        if (searchResponse.status().getStatus() == 200) {
            for (SearchHit searchHit : searchResponse.getHits().getHits()) {
                /*Map<String, SearchHitField> mapHit = searchHit.getFields();
				currentMap = new HashMap<>();
				for(Map.Entry<String, SearchHitField> hitEntry : mapHit.entrySet()){
					currentMap.put(hitEntry.getKey(), hitEntry.getValue().getValue());
				}
				listData.add(currentMap);
				*/
                listData.add(JSONObject.parseObject(searchHit.getSourceAsString(), Map.class));
            }
        }
        return listData;
    }

    /**
     * 测试检索到的记录数
     *
     * @param indexName
     * @param type
     * @param filterMap
     * @param callFields
     * @return
     */
    public long queryCountByStringQuery(Client client, String indexName, String type,
                                        Map<String, Object> filterMap, String... callFields) {
        long totalCount = 0;
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        QueryStringQueryBuilder queryString = null;
        MatchAllQueryBuilder matchAll = null;
        if (filterMap != null && !filterMap.isEmpty()) {
            for (Map.Entry entry : filterMap.entrySet()) {
                queryString = QueryBuilders.queryStringQuery(String.valueOf(entry.getValue()));
                queryString.field(String.valueOf(entry.getKey()));
                boolQueryBuilder.must(queryString);
            }
        } else {//检索全部
            matchAll = QueryBuilders.matchAllQuery();
            boolQueryBuilder.must(matchAll);
        }
        SearchResponse response = client.prepareSearch(indexName).setTypes(type)
                .setSearchType(SearchType.QUERY_THEN_FETCH)
                .setQuery(boolQueryBuilder).execute().actionGet();
        int statusCode = response.status().getStatus();
        if (statusCode == 200 || statusCode == 201) {
            totalCount = response.getHits().getTotalHits();
        }
        return totalCount;
    }

    /**
     * 根据id删除数据
     * @param client
     * @param index
     * @param type
     * @param id
     * @return
     */
    public RestStatus deleteDoc(Client client, String index, String type, String id) {

        DeleteResponse deleteResponse  = client
                .prepareDelete()
                .setIndex(index)
                .setType(type)
                .setId(id)
                .get();

        return deleteResponse.status();
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////
    //林子渊 Add
    ////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 获取数据
     * @param client
     * @param indexName
     * @param type
     * @param criteria
     * @return
     */

    public List<String> getSort(Client client, String indexName, String type,Criteria criteria){
        List<String> list = new ArrayList<String>();
        JSONArray jsonArray = criteria.getOrderBy();
        QueryBuilder qb = QueryBuilders.matchAllQuery();
        SearchRequestBuilder srb = client.prepareSearch();
        srb.setIndices(indexName).setTypes(type).setQuery(qb).addSort("age",org.elasticsearch.search.sort.SortOrder.ASC);//setSearchType(SearchType.QUERY_THEN_FETCH);
     //   srb.addSort("age",org.elasticsearch.search.sort.SortOrder.ASC);
        srb = addSort(srb,jsonArray);
        SearchResponse searchResponse = srb.execute().actionGet();
        SearchHits searchHits = searchResponse.getHits();
        for (int i = 0; i < searchHits.getHits().length; i++) {
            System.out.println(searchHits.getHits()[i].getSourceAsString());
        }
        return list;
    }
    /**
     * 组装排序sort
     */
    private SearchRequestBuilder addSort(SearchRequestBuilder searchRequestBuilder,JSONArray esSortFileds){
        SortBuilder sortBuilder = null;
        if( esSortFileds == null || esSortFileds.isEmpty()){
            sortBuilder = SortBuilders.fieldSort("id").order(org.elasticsearch.search.sort.SortOrder.ASC);
            searchRequestBuilder.addSort(sortBuilder);
            return searchRequestBuilder;// 按照es 的默认顺序
        }else{
            String attr = "";       // 字段名称
            String type = "";      // 字段值
            Iterator<Object> it = esSortFileds.iterator();
            JSONObject jsonObject = null;
            while(it.hasNext()){
                org.elasticsearch.search.sort.SortOrder sortOrder = org.elasticsearch.search.sort.SortOrder.ASC;
                jsonObject = (JSONObject)it.next();
                attr = jsonObject.getString("attr");
                type = jsonObject.getString("sort");
                if("desc".equals(type)){
                    sortBuilder = SortBuilders.fieldSort(attr).order(org.elasticsearch.search.sort.SortOrder.DESC);
                    searchRequestBuilder.addSort(sortBuilder);
                }else if ("asc".equals(type)){
                    sortBuilder = SortBuilders.fieldSort(attr).order(org.elasticsearch.search.sort.SortOrder.ASC);
                    searchRequestBuilder.addSort(sortBuilder);
                }
            }
            return searchRequestBuilder;
        }
    }

    //es where 条件
    private SearchRequestBuilder filter(SearchRequestBuilder searchRequestBuilder,JSONArray esSortFileds){
     //   FilterBuilder filterBuilder;
        return searchRequestBuilder;
    }

    /*聚合查询,结合统计*/
    //group by
    private SearchRequestBuilder group(SearchRequestBuilder searchRequestBuilder,JSONArray groupByColumns){
        if (groupByColumns == null || groupByColumns.isEmpty()){
            return searchRequestBuilder;
        } else {
            //AggregationBuilder 聚合索引
            String aggName = "";//聚合名称
            String attr = "";       // 字段名称
            String type = "";      // 字段值
            Iterator<Object> it = groupByColumns.iterator();
            JSONObject jsonObject = null;
            AggregationBuilder addAggregation = null;
            while(it.hasNext()){
                jsonObject = (JSONObject) it.next();
                attr = jsonObject.getString("attr") ;
                aggName = attr +"Agg";
                if (addAggregation != null){
                    AggregationBuilder terms = AggregationBuilders.terms(aggName).field(attr);
                    addAggregation = addAggregation.subAggregation(terms);
                }else{
                    addAggregation = AggregationBuilders.terms(aggName).field(attr) ;
                }
            }
            searchRequestBuilder = searchRequestBuilder.addAggregation(addAggregation);
        }
        return searchRequestBuilder;
    }

    private SearchRequestBuilder setPagenation(SearchRequestBuilder searchRequestBuilder,JSONObject pagenation){
        //设置分页查询
        int page = pagenation.getInteger("startPage");
        int size = pagenation.getInteger("rows");
        int startrow = (page-1) * size ;
      //  long totalPage = (totalSize % row) == 0 ? (totalSize / row) : (totalSize / row + 1);
      //  currenSize = startPage < totalPage ? row : (totalSize - (startPage - 1) * row);
        //轻分页，以后有时间改成深度分页优化大数据的查询
        searchRequestBuilder.setFrom(startrow).setSize(size);
        return searchRequestBuilder;
    }
    /**
     * @param field
     * @param operator
     * @param value
     * @return
     */
    private AggregationBuilder rangfind(String field,String operator,String value){
        Double doubleValue = Double.valueOf(value);
        AggregationBuilder aggregationBuilder = null;
        String agg  = field + "_agg";
        if ("<".equals(operator)){
            //doubleValue到正无穷
            aggregationBuilder = AggregationBuilders.range(agg).field(field).addUnboundedTo(doubleValue);
        }else if("<=".equals(operator)) {
            //doubleValue到正无穷
            aggregationBuilder = AggregationBuilders.range(agg).field(field).addUnboundedTo(doubleValue);
        }else if (">".equals(operator)){
            //负无穷到doubleValue
            aggregationBuilder = AggregationBuilders.range(agg).field(field).addUnboundedFrom(doubleValue);
        }else if (">=".equals(operator)){
            //负无穷到doubleValue
            aggregationBuilder = AggregationBuilders.range(agg).field(field).addUnboundedFrom(doubleValue);
        }else{
            //aggregationBuilder = AggregationBuilders.range(agg).field(field).addRange(min,max);
            return  null;
        }
        return aggregationBuilder;
    }

    /**
     * 分组分类(组装有问题)
     * @param field
     * @param operator
     * @param value
     * @return
     */
    private AggregationBuilder dataRangFind(String field,String operator,String value){
        String agg = "agg_" + field ;
        AggregationBuilder aggregationBuilder = null;
        if ("<=".equals(operator)){
            value = value.substring(0,4);
            aggregationBuilder= AggregationBuilders.dateRange(agg).field(field).format("yyyy").addUnboundedTo(value);
            //"yyyy-MM-dd HH:mm:ss"
        }else if(">=".equals(operator)){
            aggregationBuilder= AggregationBuilders.dateRange(agg).field(field).format("yyyy-MM-dd").addUnboundedFrom(value);
        }
        return aggregationBuilder;
    }

    /**
     * 范围查询
     * @param field
     * @param operator
     * @param value
     * @return
     */
    private QueryBuilder rangQbFind(String field,String operator,String value) {
        QueryBuilder rangQb = null;
        if (">".equals(operator)){
            rangQb =  QueryBuilders.rangeQuery(field).gt(value);
        }else if("<".equals(operator)){
            rangQb =  QueryBuilders.rangeQuery(field).lt(value);
        }else if("<=".equals(operator)){
            rangQb =  QueryBuilders.rangeQuery(field).lte(value);
        }else if(">=".equals(operator)){
            rangQb =  QueryBuilders.rangeQuery(field).gte(value);
        }
        return rangQb;
    }

    public JSONObject query(Client client, String indexName, Criteria criteria){
        JSONObject esResult = new JSONObject();
        //  getSort(client, indexName, indexName ,criteria); //排序测试函数
        JSONArray conditions = criteria.getConditions();        //  条件
        JSONObject pagenation = criteria.getPagenation();       // 分页信息
        JSONArray orderBys = criteria.getOrderBy();             // 排序信息

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        Iterator<Object> entries = conditions.iterator();
        JSONObject json = null;		// 数据行

        String attr = "";       // 字段名称
        String value = "";      // 字段值
        String operator = "";   // 操作符
        List<String> listData = null;
        //es 初始化,设置index 和 type
        SearchRequestBuilder srb = client.prepareSearch().setIndices(indexName).setTypes(indexName);
   //     List<QueryBuilder> listRangQb = new ArrayList<QueryBuilder>();
        //条件设置
        //设置where 查询条件
   //     AggregationBuilder totalAgg = AggregationBuilders.range("aggs");
        
        //匹配查询
        if (conditions.size() > 0 ){
            //进行数据过滤组装
            while (entries.hasNext()) {
                json = (JSONObject)entries.next();
                attr = json.getString("attr");
                value = json.getString("value");
                operator = json.getString("operator");
                BoolQueryBuilder qb = QueryBuilders.boolQuery();
                if ("=".equals(operator)) {
                    qb.should(QueryBuilders.termQuery(attr, value));//完全匹配(字符)
                    //  非完全匹配
                    //   qb.should(QueryBuilders.matchQuery(attr,value));
                    //     qb.should(QueryBuilders.matchPhraseQuery(attr,value));
                }else if ("like".equals(operator)){
                    qb.should(QueryBuilders.matchPhrasePrefixQuery(attr, value));
                }else if (">".equals(operator) || "<".equals(operator) || "<=".equals(operator) || ">=".equals(operator)){
                    QueryBuilder rangQb =  rangQbFind(attr,operator,value);
                    qb.must(rangQb);
                 //   listRangQb.add(rangQb);
                }else{
                    //throws new Exeception("文件");
                }
                boolQueryBuilder.must(qb);
            }
            srb.setQuery(boolQueryBuilder);
        }else{
            QueryBuilder qb = QueryBuilders.matchAllQuery();
            srb.setQuery(qb);
        }
        //范围过滤(范围过滤好像只能进行一列条件过滤),两列好像组装不了(待研究)
        // 过滤不能两列
         // srb = srb.setPostFilter(rangQb);
        //添加排序SortBuilder
        if(orderBys != null){
            srb = addSort(srb,orderBys);
        }
        // srb = group(srb,orderBys);
        //获取命中的总数
        long total = srb.execute().actionGet().getHits().getTotalHits();
        esResult.put("total",total);
        //分页
        if (pagenation != null && !pagenation.isEmpty()){
            srb = setPagenation(srb,pagenation);
        }else{
            srb.setSize(new Long(total).intValue());//long 超过int 最大值的话会怎么样呢
        }
        SearchResponse searchResponse = srb.execute().actionGet();
        SearchHits searchHits = searchResponse.getHits();
        listData = new ArrayList<String>();
        int count = searchHits.getHits().length;
        for (int i = 0; i < count; i++) {
            String rowkey = searchHits.getHits()[i].getId();
            listData.add(rowkey);
            System.out.println("rowkey:"+rowkey);
        }
        esResult.put("hits",count);//查询到的数据
        esResult.put("list",listData);//查询到的数据
        return esResult;
    }
}
