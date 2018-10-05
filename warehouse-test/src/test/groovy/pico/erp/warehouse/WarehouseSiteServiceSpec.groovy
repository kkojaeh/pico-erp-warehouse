package pico.erp.warehouse

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.shared.IntegrationConfiguration
import pico.erp.warehouse.location.site.*
import spock.lang.Specification

@SpringBootTest(classes = [IntegrationConfiguration])
@Transactional
@Rollback
@ActiveProfiles("test")
@Configuration
@ComponentScan("pico.erp.config")
class WarehouseSiteServiceSpec extends Specification {

  @Autowired
  WarehouseSiteService warehouseSiteService

  def warehouseSiteId = WarehouseSiteId.from("A2")

  def setup() {
    warehouseSiteService.create(new WarehouseSiteRequests.CreateRequest(
      id: warehouseSiteId,
      code: WarehouseSiteCode.from("A2"),
      name: "안성 2창고"
    ))
  }

  def "아이디로 존재하는 창고지 확인"() {
    when:
    def exists = warehouseSiteService.exists(warehouseSiteId)

    then:
    exists == true
  }

  def "아이디로 존재하지 않는 창고지 확인"() {
    when:
    def exists = warehouseSiteService.exists(WarehouseSiteId.from("packaging-00"))

    then:
    exists == false
  }

  def "아이디로 존재하는 창고지를 조회"() {
    when:
    def site = warehouseSiteService.get(warehouseSiteId)

    then:
    site.name == "안성 2창고"
    site.code == WarehouseSiteCode.from("A2")
  }

  def "아이디로 존재하지 않는 창고지를 조회"() {
    when:
    warehouseSiteService.get(WarehouseSiteId.from("packaging-00"))

    then:
    thrown(WarehouseSiteExceptions.NotFoundException)
  }

}
