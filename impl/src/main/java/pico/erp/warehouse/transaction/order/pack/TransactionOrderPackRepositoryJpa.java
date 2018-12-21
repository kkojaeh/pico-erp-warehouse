package pico.erp.warehouse.transaction.order.pack;

import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.warehouse.pack.PackId;
import pico.erp.warehouse.transaction.order.TransactionOrderId;


@Repository
interface TransactionOrderPackEntityRepository extends
  CrudRepository<TransactionOrderPackEntity, TransactionOrderPackId> {

  @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM TransactionOrderPack p WHERE p.orderId = :orderId AND p.packId = :packId")
  boolean exists(@Param("orderId") TransactionOrderId orderId,
    @Param("packId") PackId packId);

  @Query("SELECT p FROM TransactionOrderPack p WHERE p.orderId = :orderId")
  Stream<TransactionOrderPackEntity> findAllBy(
    @Param("orderId") TransactionOrderId orderId);

}

@Repository
@Transactional
public class TransactionOrderPackRepositoryJpa implements
  TransactionOrderPackRepository {

  @Autowired
  private TransactionOrderPackEntityRepository repository;

  @Autowired
  private TransactionOrderPackMapper mapper;

  @Override
  public TransactionOrderPack create(@NotNull TransactionOrderPack domain) {
    val entity = mapper.jpa(domain);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }

  @Override
  public void deleteBy(@NotNull TransactionOrderPackId id) {
    repository.delete(id);
  }

  @Override
  public boolean exists(@NotNull TransactionOrderPackId id) {
    return repository.exists(id);
  }

  @Override
  public boolean exists(TransactionOrderId orderId, PackId packId) {
    return repository.exists(orderId, packId);
  }

  @Override
  public Stream<TransactionOrderPack> findAllBy(
    TransactionOrderId orderId) {
    return repository
      .findAllBy(orderId)
      .map(mapper::jpa);
  }

  @Override
  public Optional<TransactionOrderPack> findBy(
    @NotNull TransactionOrderPackId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::jpa);
  }

  @Override
  public void update(@NotNull TransactionOrderPack domain) {
    val entity = repository.findOne(domain.getId());
    mapper.pass(mapper.jpa(domain), entity);
    repository.save(entity);
  }

}
