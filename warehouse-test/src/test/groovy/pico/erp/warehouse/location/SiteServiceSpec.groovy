package pico.erp.warehouse.location

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
class SiteServiceSpec extends Specification {

  @Autowired
  SiteService warehouseSiteService

  def warehouseSiteId = SiteId.from("A2")

  def setup() {
    warehouseSiteService.create(new SiteRequests.CreateRequest(
      id: warehouseSiteId,
      code: SiteCode.from("A2"),
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
    def exists = warehouseSiteService.exists(SiteId.from("packaging-00"))

    then:
    exists == false
  }

  def "아이디로 존재하는 창고지를 조회"() {
    when:
    def site = warehouseSiteService.get(warehouseSiteId)

    then:
    site.name == "안성 2창고"
    site.code == SiteCode.from("A2")
  }

  def "아이디로 존재하지 않는 창고지를 조회"() {
    when:
    warehouseSiteService.get(SiteId.from("packaging-00"))

    then:
    thrown(SiteExceptions.NotFoundException)
  }

  def "창고지를 전체 조회"() {
    when:
    def sites = warehouseSiteService.getAll()

    then:
    sites.size() == 2
  }

  def "중복 코드를 생성하면 오류 발생"() {
    when:
    warehouseSiteService.create(new SiteRequests.CreateRequest(
      id: SiteId.from("A3"),
      code: SiteCode.from("A2"),
      name: "안성 2창고1"
    ))
    then:
    thrown(SiteExceptions.CodeAlreadyExistsException)
  }

  def "이미 존재하는 코드로 변경하면 오류 발생"() {
    when:
    warehouseSiteService.create(new SiteRequests.CreateRequest(
      id: SiteId.from("A3"),
      code: SiteCode.from("A3"),
      name: "안성 3창고"
    ))
    warehouseSiteService.update(new SiteRequests.UpdateRequest(
      id: SiteId.from("A3"),
      code: SiteCode.from("A2"),
      name: "안성 3창고"
    ))
    then:
    thrown(SiteExceptions.CodeAlreadyExistsException)
  }

  def "하위 장소가 존재하는 창고를 삭제"() {
    when:
    warehouseSiteService.delete(new SiteRequests.DeleteRequest(
      id: SiteId.from("A1")
    ))
    def site = warehouseSiteService.get(SiteId.from("A1"))

    then:
    site.deleted == true
    warehouseSiteService.getAll().size() == 1
  }

}
