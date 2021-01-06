package com.sumu.jobserver.api.vo.dag;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-06 14:35
 */
public class OptionVO {
    private TitleVO title;
    private int animationDurationUpdate = 1500;
    private String animationEasingUpdate = "quinticInOut";
    private SeriesVO series;

    public TitleVO getTitle() {
        return title;
    }

    public void setTitle(TitleVO title) {
        this.title = title;
    }

    public int getAnimationDurationUpdate() {
        return animationDurationUpdate;
    }

    public void setAnimationDurationUpdate(int animationDurationUpdate) {
        this.animationDurationUpdate = animationDurationUpdate;
    }

    public String getAnimationEasingUpdate() {
        return animationEasingUpdate;
    }

    public void setAnimationEasingUpdate(String animationEasingUpdate) {
        this.animationEasingUpdate = animationEasingUpdate;
    }

    public SeriesVO getSeries() {
        return series;
    }

    public void setSeries(SeriesVO series) {
        this.series = series;
    }
}
