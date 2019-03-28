package pico.erp.warehouse.transaction.order.item.lot;

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
import pico.erp.item.lot.ItemLotId;
import pico.erp.warehouse.transaction.order.TransactionOrderId;
import pico.erp.warehouse.transaction.order.item.TransactionOrderItemId;


@Repository
interface TransactionOrderItemLotEntityRepository extends
  CrudRepository<TransactionOrderItemLotEntity, TransactionOrderItemLotId> {

  @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM TransactionOrderItemLot l WHERE l.orderItemId = :orderItemId AND l.itemLotId = :itemLotId")
  boolean exists(
    @Param("orderItemId") TransactionOrderItemId orderItemId,
    @Param("itemLotId") ItemLotId itemLotId);

  @Query("SELECT l FROM TransactionOrderItemLot l WHERE l.orderItemId = :orderItemId")
  Stream<TransactionOrderItemLotEntity> findAllBy(
    @Param("orderItemId") TransactionOrderItemId orderItemId);

  @Query("SELECT l FROM TransactionOrderItemLot l WHERE l.orderId = :orderId")
  Stream<TransactionOrderItemLotEntity> findAllBy(
    @Param("orderId") TransactionOrderId orderId);

}

@Repository
@Transactional
public class TransactionOrderItemLotRepositoryJpa implements
  TransactionOrderItemLotRepository {

  @Autowired
  private TransactionOrderItemLotEntityRepository repository;

  @Autowired
  private TransactionOrderItemLotMapper mapper;

  @Override
  public TransactionOrderItemLot create(
    @NotNull TransactionOrderItemLot domain) {
    val entity = mapper.jpa(domain);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }

  @Override
  public void deleteBy(@NotNull TransactionOrderItemLotId id) {
    repository.deleteById(id);
  }

  @Override
  public boolean exists(@NotNull TransactionOrderItemLotId id) {
    return repository.existsById(id);
  }

  @Override
  public boolean exists(TransactionOrderItemId orderItemId,
    ItemLotId itemLotId) {
    return repository.exists(orderItemId, itemLotId);
  }

  @Override
  public Stream<TransactionOrderItemLot> findAllBy(
    TransactionOrderItemId orderItemId) {
    return repository.findAllBy(orderItemId)
      .map(mapper::jpa);
  }

  @Override
  public Stream<TransactionOrderItemLot> findAllBy(
    TransactionOrderId orderId) {
    return repository.findAllBy(orderId)
      .map(mapper::jpa);
  }

  @Override
  public Optional<TransactionOrderItemLot> findBy(
    @NotNull TransactionOrderItemLotId id) {
    return repository.findById(id)
      .map(mapper::jpa);
  }

  @Override
  public void update(@NotNull TransactionOrderItemLot domain) {
    val entity = repository.findById(domain.getId()).get();
    mapper.pass(mapper.jpa(domain), entity);
    repository.save(entity);
  }

}
