package com.firm.brokage.service.demo.mappers;

import com.firm.brokage.service.demo.api.AssetResponse;
import com.firm.brokage.service.demo.entities.Asset;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AssetMapper {
    AssetResponse assetToAssetResponse(Asset asset);

    default OffsetDateTime map(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toInstant().atZone(ZoneId.systemDefault()).toOffsetDateTime();
    }
}

