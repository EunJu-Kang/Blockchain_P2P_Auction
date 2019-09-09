package com.bcauction.application;

import com.bcauction.domain.FabricAsset;

import java.util.List;

public interface IFabricCCService
{
	FabricAsset registerOwnership(final long 소유자id, final long 작품id);
	List<FabricAsset> transferOwnership(final long from, final long to, final long 작품id);
	FabricAsset expireOwnership(final long 작품id, final long 소유자id);
	FabricAsset query(final long 작품id);
	List<FabricAsset> queryHistory(final long 작품id);
}
