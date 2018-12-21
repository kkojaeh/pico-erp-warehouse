package pico.erp.warehouse.pack;

import pico.erp.shared.data.LocalizedNameable;

public enum PackStatusKind implements LocalizedNameable {

  EMPTY,

  STOCKED,

  AWAITING_INBOUND,

  AWAITING_OUTBOUND

}
