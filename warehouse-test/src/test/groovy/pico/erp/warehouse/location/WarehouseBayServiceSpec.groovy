package pico.erp.warehouse.location

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.shared.IntegrationConfiguration
import pico.erp.warehouse.location.WarehouseLocationCode
import pico.erp.warehouse.location.bay.*
import pico.erp.warehouse.location.rack.WarehouseRackId
import spock.lang.Specification

@SpringBootTest(classes = [IntegrationConfiguration])
@Transactional
@Rollback
@ActiveProfiles("test")
@Configuration
@ComponentScan("pico.erp.config")
class WarehouseBayServiceSpec extends Specification {

  @Autowired
  WarehouseBayService warehouseBayService

  def warehouseRackId = WarehouseRackId.from("A-1")

  def warehouseBayId = WarehouseBayId.from("A-1-99")

  def setup() {
    warehouseBayService.create(new WarehouseBayRequests.CreateRequest(
      id: warehouseBayId,
      rackId: warehouseRackId,
      code: WarehouseBayCode.from(99),
    ))
  }

  def "아이디로 존재하는 창고지 확인"() {
    when:
    def exists = warehouseBayService.exists(warehouseBayId)

    then:
    exists == true
  }

  def "아이디로 존재하지 않는 창고지 확인"() {
    when:
    def exists = warehouseBayService.exists(WarehouseBayId.from("unknown"))

    then:
    exists == false
  }

  def "아이디로 존재하는 창고지를 조회"() {
    when:
    def bay = warehouseBayService.get(warehouseBayId)

    then:
    bay.locationCode == WarehouseLocationCode.from("A1-A-01-99")
    bay.code == WarehouseBayCode.from(99)
  }

  def "아이디로 존재하지 않는 창고지를 조회"() {
    when:
    warehouseBayService.get(WarehouseBayId.from("unknown"))

    then:
    thrown(WarehouseBayExceptions.NotFoundException)
  }

  def "중복 코드를 생성하면 오류 발생"() {
    when:
    warehouseBayService.create(new WarehouseBayRequests.CreateRequest(
      id: WarehouseBayId.from("A-1-100"),
      rackId: warehouseRackId,
      code: WarehouseBayCode.from(99)
    ))
    then:
    thrown(WarehouseBayExceptions.CodeAlreadyExistsException)
  }

  def "이미 존재하는 코드로 변경하면 오류 발생"() {
    when:
    warehouseBayService.create(new WarehouseBayRequests.CreateRequest(
      id: WarehouseBayId.from("A-1-100"),
      rackId: warehouseRackId,
      code: WarehouseBayCode.from(98)
    ))
    warehouseBayService.update(new WarehouseBayRequests.UpdateRequest(
      id: WarehouseBayId.from("A-1-100"),
      code: WarehouseBayCode.from(99)
    ))
    then:
    thrown(WarehouseBayExceptions.CodeAlreadyExistsException)
  }

}
