package com.sumu.jobserver.api.vo.dag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-06 14:37
 */
public class SeriesVO {

    private String type = "graph";
    private String layout = "none";
    private int symbolSize = 50;
    private boolean roam = true;
    private Map<String, Object> label = new HashMap();
    private String[] edgeSymbol = {"circle", "arrow"};
    private Integer[] edgeSymbolSize = {4, 10};
    private List<DataVO> data;
    private List<LinksVO> links;
    private Map<String, Object> edgeLabel = new HashMap();
    private Map<String, Object> lineStyle = new HashMap<>();

    {
        label.put("show", true);
        label.put("position", "bottom");
        edgeLabel.put("fontSize", 20);
        lineStyle.put("opacity", 0.9);
        lineStyle.put("width", 2);
        lineStyle.put("curveness", 0);
    }

    public void setData(List<DataVO> data) {
        this.data = data;
    }

    public void setLinks(List<LinksVO> links) {
        this.links = links;
    }

    public String getType() {
        return type;
    }

    public String getLayout() {
        return layout;
    }

    public int getSymbolSize() {
        return symbolSize;
    }

    public boolean isRoam() {
        return roam;
    }

    public Map<String, Object> getLabel() {
        return label;
    }

    public String[] getEdgeSymbol() {
        return edgeSymbol;
    }

    public Integer[] getEdgeSymbolSize() {
        return edgeSymbolSize;
    }

    public List<DataVO> getData() {
        return data;
    }

    public List<LinksVO> getLinks() {
        return links;
    }

    public Map<String, Object> getEdgeLabel() {
        return edgeLabel;
    }

    public Map<String, Object> getLineStyle() {
        return lineStyle;
    }
}
