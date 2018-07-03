
package com.test.pokedex.network.models.pokemon;

import com.google.gson.annotations.SerializedName;

public class VersionGroupDetail {

    @SerializedName("level_learned_at")
    private Long mLevelLearnedAt;
    @SerializedName("move_learn_method")
    private MoveLearnMethod mMoveLearnMethod;
    @SerializedName("version_group")
    private VersionGroup mVersionGroup;

    public Long getLevelLearnedAt() {
        return mLevelLearnedAt;
    }

    public void setLevelLearnedAt(Long levelLearnedAt) {
        mLevelLearnedAt = levelLearnedAt;
    }

    public MoveLearnMethod getMoveLearnMethod() {
        return mMoveLearnMethod;
    }

    public void setMoveLearnMethod(MoveLearnMethod moveLearnMethod) {
        mMoveLearnMethod = moveLearnMethod;
    }

    public VersionGroup getVersionGroup() {
        return mVersionGroup;
    }

    public void setVersionGroup(VersionGroup versionGroup) {
        mVersionGroup = versionGroup;
    }

}
