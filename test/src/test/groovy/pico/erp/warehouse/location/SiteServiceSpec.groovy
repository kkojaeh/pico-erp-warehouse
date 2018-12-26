package pico.erp.warehouse.location

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.shared.IntegrationConfiguration
import pico.erp.shared.data.Address
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
  SiteService siteService

  def siteId = SiteId.from("A2")

  def address = new Address(
    postalCode: '13496',
    street: '경기도 성남시 분당구 장미로 42',
    detail: '야탑리더스 410호'
  )

  def setup() {
    siteService.create(new SiteRequests.CreateRequest(
      id: siteId,
      code: SiteCode.from("A2"),
      name: "안성 2창고",
      address: address
    ))
  }

  def "아이디로 존재하는 창고지 확인"() {
    when:
    def exists = siteService.exists(siteId)

    then:
    exists == true
  }

  def "아이디로 존재하지 않는 창고지 확인"() {
    when:
    def exists = siteService.exists(SiteId.from("packaging-00"))

    then:
    exists == false
  }

  def "아이디로 존재하는 창고지를 조회"() {
    when:
    def site = siteService.get(siteId)

    then:
    site.name == "안성 2창고"
    site.code == SiteCode.from("A2")
  }

  def "아이디로 존재하지 않는 창고지를 조회"() {
    when:
    siteService.get(SiteId.from("packaging-00"))

    then:
    thrown(SiteExceptions.NotFoundException)
  }

  def "창고지를 전체 조회"() {
    when:
    def sites = siteService.getAll()

    then:
    sites.size() == 3
  }

  def "중복 코드를 생성하면 오류 발생"() {
    when:
    siteService.create(new SiteRequests.CreateRequest(
      id: SiteId.from("A3"),
      code: SiteCode.from("A2"),
      name: "안성 2창고1",
      address: address
    ))
    then:
    thrown(SiteExceptions.CodeAlreadyExistsException)
  }

  def "이미 존재하는 코드로 변경하면 오류 발생"() {
    when:
    siteService.create(new SiteRequests.CreateRequest(
      id: SiteId.from("A3"),
      code: SiteCode.from("A3"),
      name: "안성 3창고",
      address: address
    ))
    siteService.update(new SiteRequests.UpdateRequest(
      id: SiteId.from("A3"),
      code: SiteCode.from("A2"),
      name: "안성 3창고",
      address: address
    ))
    then:
    thrown(SiteExceptions.CodeAlreadyExistsException)
  }

  def "하위 장소가 존재하는 창고를 삭제"() {
    when:
    siteService.delete(new SiteRequests.DeleteRequest(
      id: SiteId.from("A1")
    ))
    def site = siteService.get(SiteId.from("A1"))

    then:
    site.deleted == true
    siteService.getAll().size() == 2
  }

}
