package pico.erp.warehouse

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.shared.IntegrationConfiguration
import pico.erp.warehouse.location.WarehouseLocationCode
import pico.erp.warehouse.location.bay.WarehouseBayId
import pico.erp.warehouse.location.level.*
import spock.lang.Specification

@SpringBootTest(classes = [IntegrationConfiguration])
@Transactional
@Rollback
@ActiveProfiles("test")
@Configuration
@ComponentScan("pico.erp.config")
class WarehouseLevelServiceSpec extends Specification {

  @Autowired
  WarehouseLevelService warehouseLevelService

  def warehouseBayId = WarehouseBayId.from("A-1-1")

  def warehouseLevelId = WarehouseLevelId.from("A-1-1-20")

  def setup() {
    warehouseLevelService.create(new WarehouseLevelRequests.CreateRequest(
      id: warehouseLevelId,
      bayId: warehouseBayId,
      code: WarehouseLevelCode.from(20),
    ))
  }

  def "아이디로 존재하는 창고지 확인"() {
    when:
    def exists = warehouseLevelService.exists(warehouseLevelId)

    then:
    exists == true
  }

  def "아이디로 존재하지 않는 창고지 확인"() {
    when:
    def exists = warehouseLevelService.exists(WarehouseLevelId.from("unknown"))

    then:
    exists == false
  }

  def "아이디로 존재하는 창고지를 조회"() {
    when:
    def rack = warehouseLevelService.get(warehouseLevelId)

    then:
    rack.locationCode == WarehouseLocationCode.from("A1-A-01-01-20")
    rack.code == WarehouseLevelCode.from(20)
  }

  def "아이디로 존재하지 않는 창고지를 조회"() {
    when:
    warehouseLevelService.get(WarehouseLevelId.from("unknown"))

    then:
    thrown(WarehouseLevelExceptions.NotFoundException)
  }

}
