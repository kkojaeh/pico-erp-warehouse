package pico.erp.warehouse.transaction.order;

import java.time.OffsetDateTime;
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


@Repository
interface WarehouseTransactionOrderEntityRepository extends
  CrudRepository<WarehouseTransactionOrderEntity, WarehouseTransactionOrderId> {

  @Query("SELECT o FROM WarehouseTransactionOrder o WHERE o.status = :status AND o.dueDate < :fixedDate")
  Stream<WarehouseTransactionOrderEntity> findAllDueDateBeforeThan(
    @Param("fixedDate") OffsetDateTime fixedDate,
    @Param("status") WarehouseTransactionOrderStatusKind status);

}

@Repository
@Transactional
public class WarehouseTransactionOrderRepositoryJpa implements
  WarehouseTransactionOrderRepository {

  @Autowired
  private WarehouseTransactionOrderEntityRepository repository;

  @Autowired
  private WarehouseTransactionOrderMapper mapper;

  @Override
  public WarehouseTransactionOrder create(
    @NotNull WarehouseTransactionOrder transactionOrder) {
    val entity = mapper.jpa(transactionOrder);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }

  @Override
  public void deleteBy(@NotNull WarehouseTransactionOrderId id) {
    repository.delete(id);
  }

  @Override
  public boolean exists(@NotNull WarehouseTransactionOrderId id) {
    return repository.exists(id);
  }

  @Override
  public Optional<WarehouseTransactionOrderAggregator> findAggregatorBy(
    WarehouseTransactionOrderId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::aggregator);
  }

  @Override
  public Stream<WarehouseTransactionOrder> findAllUncommittedAt(OffsetDateTime fixedDate) {
    return repository
      .findAllDueDateBeforeThan(fixedDate, WarehouseTransactionOrderStatusKind.CREATED)
      .map(mapper::jpa);
  }

  @Override
  public Optional<WarehouseTransactionOrder> findBy(@NotNull WarehouseTransactionOrderId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::jpa);
  }

  @Override
  public void update(@NotNull WarehouseTransactionOrder transactionOrder) {
    val entity = repository.findOne(transactionOrder.getId());
    mapper.pass(mapper.jpa(transactionOrder), entity);
    repository.save(entity);
  }

}
