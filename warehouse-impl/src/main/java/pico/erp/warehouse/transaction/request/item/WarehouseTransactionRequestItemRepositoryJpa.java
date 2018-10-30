package pico.erp.warehouse.transaction.request.item;

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
import pico.erp.warehouse.transaction.request.WarehouseTransactionRequestId;


@Repository
interface WarehouseTransactionRequestItemEntityRepository extends
  CrudRepository<WarehouseTransactionRequestItemEntity, WarehouseTransactionRequestItemId> {

  @Query("SELECT CASE WHEN COUNT(wtri) > 0 THEN true ELSE false END FROM WarehouseTransactionRequestItem wtri WHERE wtri.transactionRequestId = :transactionRequestId AND wtri.itemId = :itemId")
  boolean exists(@Param("transactionRequestId") WarehouseTransactionRequestId transactionRequestId,
    @Param("itemId") ItemId itemId);

  @Query("SELECT wtri FROM WarehouseTransactionRequestItem wtri WHERE wtri.transactionRequestId = :transactionRequestId")
  Stream<WarehouseTransactionRequestItemEntity> findAllBy(
    @Param("transactionRequestId") WarehouseTransactionRequestId transactionRequestId);

}

@Repository
@Transactional
public class WarehouseTransactionRequestItemRepositoryJpa implements
  WarehouseTransactionRequestItemRepository {

  @Autowired
  private WarehouseTransactionRequestItemEntityRepository repository;

  @Autowired
  private WarehouseTransactionRequestItemMapper mapper;

  @Override
  public WarehouseTransactionRequestItem create(@NotNull WarehouseTransactionRequestItem domain) {
    val entity = mapper.jpa(domain);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }

  @Override
  public void deleteBy(@NotNull WarehouseTransactionRequestItemId id) {
    repository.delete(id);
  }

  @Override
  public boolean exists(@NotNull WarehouseTransactionRequestItemId id) {
    return repository.exists(id);
  }

  @Override
  public boolean exists(WarehouseTransactionRequestId transactionRequestId, ItemId itemId) {
    return repository.exists(transactionRequestId, itemId);
  }

  @Override
  public Stream<WarehouseTransactionRequestItem> findAllBy(
    WarehouseTransactionRequestId transactionRequestId) {
    return repository
      .findAllBy(transactionRequestId)
      .map(mapper::jpa);
  }

  @Override
  public Optional<WarehouseTransactionRequestItem> findBy(
    @NotNull WarehouseTransactionRequestItemId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::jpa);
  }

  @Override
  public void update(@NotNull WarehouseTransactionRequestItem domain) {
    val entity = repository.findOne(domain.getId());
    mapper.pass(mapper.jpa(domain), entity);
    repository.save(entity);
  }

}
