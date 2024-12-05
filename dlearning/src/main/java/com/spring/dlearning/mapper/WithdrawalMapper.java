package com.spring.dlearning.mapper;

import com.spring.dlearning.dto.request.WithdrawalRequest;
import com.spring.dlearning.dto.response.WithdrawalHistoryResponse;
import com.spring.dlearning.dto.response.WithdrawalResponse;
import com.spring.dlearning.entity.WithdrawalHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WithdrawalMapper {

    WithdrawalHistory toWithdrawalHistory(WithdrawalRequest withdrawalRequest);

    WithdrawalResponse toWithdrawalResponse(WithdrawalHistory withdrawalHistory);

    @Mapping(source = "user.name", target = "name")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "createdAt", target = "createdAt")
    WithdrawalHistoryResponse toWithdrawalHistoryResponse(WithdrawalHistory withdrawalHistory);

}
