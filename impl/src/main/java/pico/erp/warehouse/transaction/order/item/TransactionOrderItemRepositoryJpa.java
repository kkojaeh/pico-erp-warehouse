package pico.erp.warehouse.transaction.order.item;

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
import pico.erp.item.ItemId;
import pico.erp.warehouse.transaction.order.TransactionOrderId;


@Repository
interface TransactionOrderItemEntityRepository extends
  CrudRepository<TransactionOrderItemEntity, TransactionOrderItemId> {

  @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END FROM TransactionOrderItem i WHERE i.orderId = :orderId AND i.itemId = :itemId")
  boolean exists(@Param("orderId") TransactionOrderId orderId,
    @Param("itemId") ItemId itemId);

  @Query("SELECT i FROM TransactionOrderItem i WHERE i.orderId = :orderId")
  Stream<TransactionOrderItemEntity> findAllBy(
    @Param("orderId") TransactionOrderId orderId);

}

@Repository
@Transactional
public class TransactionOrderItemRepositoryJpa implements
  TransactionOrderItemRepository {

  @Autowired
  private TransactionOrderItemEntityRepository repository;

  @Autowired
  private TransactionOrderItemMapper mapper;

  @Override
  public TransactionOrderItem create(@NotNull TransactionOrderItem domain) {
    val entity = mapper.jpa(domain);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }

  @Override
  public void deleteBy(@NotNull TransactionOrderItemId id) {
    repository.deleteById(id);
  }

  @Override
  public boolean exists(@NotNull TransactionOrderItemId id) {
    return repository.existsById(id);
  }

  @Override
  public boolean exists(TransactionOrderId transactionOrderId, ItemId itemId) {
    return repository.exists(transactionOrderId, itemId);
  }

  @Override
  public Stream<TransactionOrderItem> findAllBy(
    TransactionOrderId transactionOrderId) {
    return repository
      .findAllBy(transactionOrderId)
      .map(mapper::jpa);
  }

  @Override
  public Optional<TransactionOrderItem> findBy(
    @NotNull TransactionOrderItemId id) {
    return repository.findById(id)
      .map(mapper::jpa);
  }

  @Override
  public void update(@NotNull TransactionOrderItem domain) {
    val entity = repository.findById(domain.getId()).get();
    mapper.pass(mapper.jpa(domain), entity);
    repository.save(entity);
  }

}
