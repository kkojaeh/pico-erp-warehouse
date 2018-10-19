package pico.erp.warehouse

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.shared.IntegrationConfiguration
import pico.erp.warehouse.location.WarehouseLocationId
import pico.erp.warehouse.location.WarehouseLocationService
import pico.erp.warehouse.location.bay.WarehouseBayId
import pico.erp.warehouse.location.level.WarehouseLevelCode
import pico.erp.warehouse.location.level.WarehouseLevelId
import pico.erp.warehouse.location.level.WarehouseLevelRequests
import pico.erp.warehouse.location.level.WarehouseLevelService
import spock.lang.Specification

@SpringBootTest(classes = [IntegrationConfiguration])
@Transactional
@Rollback
@ActiveProfiles("test")
@Configuration
@ComponentScan("pico.erp.config")
class WarehouseLocationServiceSpec extends Specification {

  @Autowired
  WarehouseLevelService warehouseLevelService

  @Autowired
  WarehouseLocationService warehouseLocationService

  def warehouseBayId = WarehouseBayId.from("A-1-1")

  def warehouseLevelId = WarehouseLevelId.from("A-1-1-20")

  def warehouseLocationId = WarehouseLocationId.from("A-1-1-20")

  def setup() {
    warehouseLevelService.create(new WarehouseLevelRequests.CreateRequest(
      id: warehouseLevelId,
      bayId: warehouseBayId,
      code: WarehouseLevelCode.from(20),
    ))
  }

  def "레벨을 생성하면 동일 아이디로 위치가 존재"() {
    when:

    def exists = warehouseLocationService.exists(warehouseLocationId)

    then:
    exists == true
  }

  def "레벨을 생성하면 동일 아이디의 위치와 값이 동일"() {
    when:
    def level = warehouseLevelService.get(warehouseLevelId)
    def location = warehouseLocationService.get(warehouseLocationId)

    then:
    level.id.value == location.id.value
    level.locationCode == location.code
  }


}
