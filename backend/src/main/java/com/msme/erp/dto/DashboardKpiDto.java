package com.msme.erp.dto;

import java.util.List;
import java.util.Map;

public class DashboardKpiDto {
    private long totalOrders;
    private long activeOrders;
    private long completedOrders;
    private long blockedOrders;
    private double onTimeDeliveryRate;
    private double qcPassRate;
    private long unreadNotificationsCount;
    private Map<String, Long> stageBottlenecks;
    private List<OrderDto> recentHighPriorityOrders;
    private List<OrderStageLogDto> liveActivityFeed;

    public DashboardKpiDto() {}

    public DashboardKpiDto(long totalOrders, long activeOrders, long completedOrders, long blockedOrders, double onTimeDeliveryRate, double qcPassRate, long unreadNotificationsCount, Map<String, Long> stageBottlenecks, List<OrderDto> recentHighPriorityOrders, List<OrderStageLogDto> liveActivityFeed) {
        this.totalOrders = totalOrders;
        this.activeOrders = activeOrders;
        this.completedOrders = completedOrders;
        this.blockedOrders = blockedOrders;
        this.onTimeDeliveryRate = onTimeDeliveryRate;
        this.qcPassRate = qcPassRate;
        this.unreadNotificationsCount = unreadNotificationsCount;
        this.stageBottlenecks = stageBottlenecks;
        this.recentHighPriorityOrders = recentHighPriorityOrders;
        this.liveActivityFeed = liveActivityFeed;
    }

    public static DashboardKpiDtoBuilder builder() { return new DashboardKpiDtoBuilder(); }

    public long getTotalOrders() { return totalOrders; }
    public void setTotalOrders(long totalOrders) { this.totalOrders = totalOrders; }
    public long getActiveOrders() { return activeOrders; }
    public void setActiveOrders(long activeOrders) { this.activeOrders = activeOrders; }
    public long getCompletedOrders() { return completedOrders; }
    public void setCompletedOrders(long completedOrders) { this.completedOrders = completedOrders; }
    public long getBlockedOrders() { return blockedOrders; }
    public void setBlockedOrders(long blockedOrders) { this.blockedOrders = blockedOrders; }
    public double getOnTimeDeliveryRate() { return onTimeDeliveryRate; }
    public void setOnTimeDeliveryRate(double onTimeDeliveryRate) { this.onTimeDeliveryRate = onTimeDeliveryRate; }
    public double getQcPassRate() { return qcPassRate; }
    public void setQcPassRate(double qcPassRate) { this.qcPassRate = qcPassRate; }
    public long getUnreadNotificationsCount() { return unreadNotificationsCount; }
    public void setUnreadNotificationsCount(long unreadNotificationsCount) { this.unreadNotificationsCount = unreadNotificationsCount; }
    public Map<String, Long> getStageBottlenecks() { return stageBottlenecks; }
    public void setStageBottlenecks(Map<String, Long> stageBottlenecks) { this.stageBottlenecks = stageBottlenecks; }
    public List<OrderDto> getRecentHighPriorityOrders() { return recentHighPriorityOrders; }
    public void setRecentHighPriorityOrders(List<OrderDto> recentHighPriorityOrders) { this.recentHighPriorityOrders = recentHighPriorityOrders; }
    public List<OrderStageLogDto> getLiveActivityFeed() { return liveActivityFeed; }
    public void setLiveActivityFeed(List<OrderStageLogDto> liveActivityFeed) { this.liveActivityFeed = liveActivityFeed; }

    public static class DashboardKpiDtoBuilder {
        private long totalOrders;
        private long activeOrders;
        private long completedOrders;
        private long blockedOrders;
        private double onTimeDeliveryRate;
        private double qcPassRate;
        private long unreadNotificationsCount;
        private Map<String, Long> stageBottlenecks;
        private List<OrderDto> recentHighPriorityOrders;
        private List<OrderStageLogDto> liveActivityFeed;

        public DashboardKpiDtoBuilder totalOrders(long totalOrders) { this.totalOrders = totalOrders; return this; }
        public DashboardKpiDtoBuilder activeOrders(long activeOrders) { this.activeOrders = activeOrders; return this; }
        public DashboardKpiDtoBuilder completedOrders(long completedOrders) { this.completedOrders = completedOrders; return this; }
        public DashboardKpiDtoBuilder blockedOrders(long blockedOrders) { this.blockedOrders = blockedOrders; return this; }
        public DashboardKpiDtoBuilder onTimeDeliveryRate(double onTimeDeliveryRate) { this.onTimeDeliveryRate = onTimeDeliveryRate; return this; }
        public DashboardKpiDtoBuilder qcPassRate(double qcPassRate) { this.qcPassRate = qcPassRate; return this; }
        public DashboardKpiDtoBuilder unreadNotificationsCount(long unreadNotificationsCount) { this.unreadNotificationsCount = unreadNotificationsCount; return this; }
        public DashboardKpiDtoBuilder stageBottlenecks(Map<String, Long> stageBottlenecks) { this.stageBottlenecks = stageBottlenecks; return this; }
        public DashboardKpiDtoBuilder recentHighPriorityOrders(List<OrderDto> recentHighPriorityOrders) { this.recentHighPriorityOrders = recentHighPriorityOrders; return this; }
        public DashboardKpiDtoBuilder liveActivityFeed(List<OrderStageLogDto> liveActivityFeed) { this.liveActivityFeed = liveActivityFeed; return this; }

        public DashboardKpiDto build() {
            return new DashboardKpiDto(totalOrders, activeOrders, completedOrders, blockedOrders, onTimeDeliveryRate, qcPassRate, unreadNotificationsCount, stageBottlenecks, recentHighPriorityOrders, liveActivityFeed);
        }
    }
}
