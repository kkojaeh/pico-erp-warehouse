package pico.erp.warehouse.location

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.shared.IntegrationConfiguration
import pico.erp.warehouse.location.LocationId
import pico.erp.warehouse.location.LocationService
import pico.erp.warehouse.location.bay.BayId
import pico.erp.warehouse.location.level.LevelCode
import pico.erp.warehouse.location.level.LevelId
import pico.erp.warehouse.location.level.LevelRequests
import pico.erp.warehouse.location.level.LevelService
import spock.lang.Specification

@SpringBootTest(classes = [IntegrationConfiguration])
@Transactional
@Rollback
@ActiveProfiles("test")
@Configuration
@ComponentScan("pico.erp.config")
class LocationServiceSpec extends Specification {

  @Autowired
  LevelService warehouseLevelService

  @Autowired
  LocationService warehouseLocationService

  def warehouseBayId = BayId.from("A-1-1")

  def warehouseLevelId = LevelId.from("A-1-1-20")

  def warehouseLocationId = LocationId.from("A-1-1-20")

  def setup() {
    warehouseLevelService.create(new LevelRequests.CreateRequest(
      id: warehouseLevelId,
      bayId: warehouseBayId,
      code: LevelCode.from(20),
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
