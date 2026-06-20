package com.banquito.switchpagos.reporting.dto.response;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NoveltyDetailsResponse {

    private UUID batchId;
    private Integer totalNovelties;
    private List<NoveltyDetailResponse> novelties = new ArrayList<>();

    public UUID getBatchId() { return batchId; }
    public void setBatchId(UUID batchId) { this.batchId = batchId; }
    public Integer getTotalNovelties() { return totalNovelties; }
    public void setTotalNovelties(Integer totalNovelties) { this.totalNovelties = totalNovelties; }
    public List<NoveltyDetailResponse> getNovelties() { return novelties; }
    public void setNovelties(List<NoveltyDetailResponse> novelties) { this.novelties = novelties; }
}
