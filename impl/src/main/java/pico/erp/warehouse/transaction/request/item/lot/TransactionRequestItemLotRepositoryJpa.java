package pico.erp.warehouse.transaction.request.item.lot;

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
import pico.erp.warehouse.transaction.request.TransactionRequestId;
import pico.erp.warehouse.transaction.request.item.TransactionRequestItemId;


@Repository
interface TransactionRequestItemLotEntityRepository extends
  CrudRepository<TransactionRequestItemLotEntity, TransactionRequestItemLotId> {

  @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM TransactionRequestItemLot l WHERE l.requestItemId = :requestItemId AND l.itemLotId = :itemLotId")
  boolean exists(
    @Param("requestItemId") TransactionRequestItemId requestItemId,
    @Param("itemLotId") ItemLotId itemLotId);

  @Query("SELECT l FROM TransactionRequestItemLot l WHERE l.requestItemId = :requestItemId")
  Stream<TransactionRequestItemLotEntity> findAllBy(
    @Param("requestItemId") TransactionRequestItemId requestItemId);

  @Query("SELECT l FROM TransactionRequestItemLot l WHERE l.requestId = :requestId")
  Stream<TransactionRequestItemLotEntity> findAllBy(
    @Param("requestId") TransactionRequestId requestId);

}

@Repository
@Transactional
public class TransactionRequestItemLotRepositoryJpa implements
  TransactionRequestItemLotRepository {

  @Autowired
  private TransactionRequestItemLotEntityRepository repository;

  @Autowired
  private TransactionRequestItemLotMapper mapper;

  @Override
  public TransactionRequestItemLot create(
    @NotNull TransactionRequestItemLot domain) {
    val entity = mapper.jpa(domain);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }

  @Override
  public void deleteBy(@NotNull TransactionRequestItemLotId id) {
    repository.deleteById(id);
  }

  @Override
  public boolean exists(@NotNull TransactionRequestItemLotId id) {
    return repository.existsById(id);
  }

  @Override
  public boolean exists(TransactionRequestItemId requestItemId,
    ItemLotId itemLotId) {
    return repository.exists(requestItemId, itemLotId);
  }

  @Override
  public Stream<TransactionRequestItemLot> findAllBy(
    TransactionRequestItemId requestItemId) {
    return repository.findAllBy(requestItemId)
      .map(mapper::jpa);
  }

  @Override
  public Stream<TransactionRequestItemLot> findAllBy(
    TransactionRequestId requestId) {
    return repository.findAllBy(requestId)
      .map(mapper::jpa);
  }

  @Override
  public Optional<TransactionRequestItemLot> findBy(
    @NotNull TransactionRequestItemLotId id) {
    return repository.findById(id)
      .map(mapper::jpa);
  }

  @Override
  public void update(@NotNull TransactionRequestItemLot domain) {
    val entity = repository.findById(domain.getId()).get();
    mapper.pass(mapper.jpa(domain), entity);
    repository.save(entity);
  }

}
