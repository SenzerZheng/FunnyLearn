package net.frontdo.funnylearn.ui.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * ProjectName: GetFavState
 * Description: 获取产品收藏状态
 *
 * author: JeyZheng
 * version: 4.0
 * created at: 11/27/2016 20:47
 */
@Data
public class GetFavState implements Serializable {
    public static final int STATE_FAV = 0;          // 收藏状态：0: 已收藏
    public static final int STATE_UNFAV = 1;        // 收藏状态：1：未收藏

    private int state;                              // 收藏状态：0: 已收藏，1：未收藏
}
