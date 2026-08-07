package com.msme.erp.dto;

import java.util.List;

public class ReorderStagesRequest {
    private List<StageOrder> stageOrders;

    public ReorderStagesRequest() {}
    public ReorderStagesRequest(List<StageOrder> stageOrders) { this.stageOrders = stageOrders; }

    public List<StageOrder> getStageOrders() { return stageOrders; }
    public void setStageOrders(List<StageOrder> stageOrders) { this.stageOrders = stageOrders; }

    public static class StageOrder {
        private String stageId;
        private Integer sequenceOrder;

        public StageOrder() {}
        public StageOrder(String stageId, Integer sequenceOrder) {
            this.stageId = stageId;
            this.sequenceOrder = sequenceOrder;
        }

        public String getStageId() { return stageId; }
        public void setStageId(String stageId) { this.stageId = stageId; }
        public Integer getSequenceOrder() { return sequenceOrder; }
        public void setSequenceOrder(Integer sequenceOrder) { this.sequenceOrder = sequenceOrder; }
    }
}
