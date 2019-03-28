package pico.erp.warehouse.pack

import kkojaeh.spring.boot.component.SpringBootTestComponent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.company.CompanyApplication
import pico.erp.item.ItemApplication
import pico.erp.item.ItemId
import pico.erp.item.lot.ItemLotId
import pico.erp.shared.TestParentApplication
import pico.erp.user.UserApplication
import pico.erp.warehouse.TestConfig
import pico.erp.warehouse.WarehouseApplication
import pico.erp.warehouse.location.LocationId
import pico.erp.warehouse.transaction.TransactionQuantityCorrectionPolicyKind
import spock.lang.Specification

@SpringBootTest(classes = [WarehouseApplication, TestConfig])
@SpringBootTestComponent(parent = TestParentApplication, siblings = [ItemApplication, UserApplication, CompanyApplication])
@Transactional
@Rollback
@ActiveProfiles("test")
class PackSelectorSpec extends Specification {

  @Autowired
  PackService packService

  @Autowired
  PackSelector packSelector

  def packId = PackId.from("test")

  def packId2 = PackId.from("test-2")

  def packId3 = PackId.from("test-3")

  def locationId = LocationId.from("A-1-1-1")

  def locationId2 = LocationId.from("A-1-1-2")

  def locationId3 = LocationId.from("A-1-1-3")

  def itemId = ItemId.from("item-1")

  def itemLotId = ItemLotId.from("item-1-lot-1")

  def itemLotId2 = ItemLotId.from("item-1-lot-2")

  def setup() {
    packService.create(new PackRequests.CreateRequest(
      id: packId
    ))
    packService.pack(
      new PackRequests.PackRequest(
        id: packId,
        itemLotId: itemLotId,
        quantity: 30
      )
    )
    packService.create(new PackRequests.CreateRequest(
      id: packId2
    ))
    packService.pack(
      new PackRequests.PackRequest(
        id: packId2,
        itemLotId: itemLotId,
        quantity: 20
      )
    )
    packService.create(new PackRequests.CreateRequest(
      id: packId3
    ))
    packService.pack(
      new PackRequests.PackRequest(
        id: packId3,
        itemLotId: itemLotId2,
        quantity: 40
      )
    )
  }

  def "수량 보정없이 품목 LOT 를 기준으로 정확한 수량으로 선택"() {
    when:

    def packs = packSelector.select(
      new PackSelector.ItemLotSelectOptions(
        itemLotId: itemLotId,
        quantity: 50,
        quantityCorrectionPolicy: TransactionQuantityCorrectionPolicyKind.NEGATIVE
      )
    )

    def sum = packs
      .map { pack -> pack.quantity }
      .reduce { acc, cur -> cur + acc }
      .get()

    then:
    sum == 50
  }

  def "수량 보정없이 품목 LOT 를 기준으로 불일치 수량으로 선택"() {
    when:

    def packs = packSelector.select(
      new PackSelector.ItemLotSelectOptions(
        itemLotId: itemLotId,
        quantity: 40,
        quantityCorrectionPolicy: TransactionQuantityCorrectionPolicyKind.NEGATIVE
      )
    )

    then:
    thrown(PackExceptions.CannotSelectException)
  }

  def "미만 수량 보정으로 품목 LOT 를 기준으로 선택"() {
    when:

    def packs = packSelector.select(
      new PackSelector.ItemLotSelectOptions(
        itemLotId: itemLotId,
        quantity: 40,
        quantityCorrectionPolicy: TransactionQuantityCorrectionPolicyKind.ROUND_DOWN
      )
    )

    def sum = packs
      .map { pack -> pack.quantity }
      .reduce { acc, cur -> cur + acc }
      .get()

    then:
    sum == 30
  }

  def "초과 수량 보정으로 품목 LOT 를 기준으로 선택"() {
    when:

    def packs = packSelector.select(
      new PackSelector.ItemLotSelectOptions(
        itemLotId: itemLotId,
        quantity: 40,
        quantityCorrectionPolicy: TransactionQuantityCorrectionPolicyKind.ROUND_UP
      )
    )

    def sum = packs
      .map { pack -> pack.quantity }
      .reduce { acc, cur -> cur + acc }
      .get()

    then:
    sum == 50
  }

  def "수량 보정없이 품목을 기준으로 정확한 수량으로 선택"() {
    when:

    def packs = packSelector.select(
      new PackSelector.ItemSelectOptions(
        itemId: itemId,
        quantity: 90,
        quantityCorrectionPolicy: TransactionQuantityCorrectionPolicyKind.NEGATIVE
      )
    )

    def sum = packs
      .map { pack -> pack.quantity }
      .reduce { acc, cur -> cur + acc }
      .get()

    then:
    sum == 90
  }

  def "수량 보정없이 품목을 기준으로 불일치 수량으로 선택"() {
    when:

    def packs = packSelector.select(
      new PackSelector.ItemSelectOptions(
        itemId: itemId,
        quantity: 88,
        quantityCorrectionPolicy: TransactionQuantityCorrectionPolicyKind.NEGATIVE
      )
    )
    then:
    thrown(PackExceptions.CannotSelectException)
  }

  def "미만 수량 보정으로 품목을 기준으로 선택"() {
    when:

    def packs = packSelector.select(
      new PackSelector.ItemSelectOptions(
        itemId: itemId,
        quantity: 80,
        quantityCorrectionPolicy: TransactionQuantityCorrectionPolicyKind.ROUND_DOWN
      )
    )

    def sum = packs
      .map { pack -> pack.quantity }
      .reduce { acc, cur -> cur + acc }
      .get()

    then:
    // 70 이 아닌 50 인 이유는 LOT 기준 FIFO
    sum == 50
  }

  def "초과 수량 보정으로 품목을 기준으로 선택"() {
    when:

    def packs = packSelector.select(
      new PackSelector.ItemSelectOptions(
        itemId: itemId,
        quantity: 80,
        quantityCorrectionPolicy: TransactionQuantityCorrectionPolicyKind.ROUND_UP
      )
    )

    def sum = packs
      .map { pack -> pack.quantity }
      .reduce { acc, cur -> cur + acc }
      .get()

    then:
    sum == 90
  }


}
