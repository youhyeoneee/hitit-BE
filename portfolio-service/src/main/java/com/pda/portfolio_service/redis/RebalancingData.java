package com.pda.portfolio_service.redis;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class RebalancingData {
    private int userId;
    private List<FundData> rebalancingData;
    private List<VarianceData> variance;
    @Data
    public static class FundData {
        private Map<String, List<FundInfo>> fundData;
    }
    @Data
    public static class FundInfo {
        private String fundName;
        private String companyName;
        private double return3m;
        private List<StockInfo> stockInfo;
    }
    @Data
    public static class StockInfo {
        private List<String> stockName;
        private List<String> badNewsTitles;
        private List<String> badNewsUrls;
        private List<String> stockCodes;
        private List<String> rev;
        private List<String> income;
    }
    @Data
    public static class VarianceData {
        private Map<String, List<WeightData>> variance;
    }
    @Data
    public static class WeightData {
        private double afterWeights;
        private double beforeWeights;
    }
}
