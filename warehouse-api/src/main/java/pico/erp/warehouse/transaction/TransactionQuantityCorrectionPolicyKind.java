package pico.erp.warehouse.transaction;

import pico.erp.shared.data.LocalizedNameable;

public enum TransactionQuantityCorrectionPolicyKind implements LocalizedNameable {

  /**
   * 수량 보정 불가 수량이 정확히 일치 해야 함
   */
  NEGATIVE,

  /**
   * 수량 보정 가능 요청 수량보다 많은 수로 보정
   */
  ROUND_UP,

  /**
   * 수량 보정 가능 요청 수량보다 줄여서 보정
   */
  ROUND_DOWN;

}
