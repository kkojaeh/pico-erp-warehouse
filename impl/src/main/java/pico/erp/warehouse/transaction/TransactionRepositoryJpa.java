package pico.erp.warehouse.transaction;

import java.util.Optional;
import javax.validation.constraints.NotNull;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
interface TransactionEntityRepository extends
  CrudRepository<TransactionEntity, TransactionId> {

}

@Repository
@Transactional
public class TransactionRepositoryJpa implements TransactionRepository {

  @Autowired
  private TransactionEntityRepository repository;

  @Autowired
  private TransactionMapper mapper;

  @Override
  public Transaction create(@NotNull Transaction transaction) {
    val entity = mapper.jpa(transaction);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }

  @Override
  public void deleteBy(@NotNull TransactionId id) {
    repository.delete(id);
  }

  @Override
  public boolean exists(@NotNull TransactionId id) {
    return repository.exists(id);
  }

  @Override
  public Optional<Transaction> findBy(@NotNull TransactionId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::jpa);
  }

  @Override
  public void update(@NotNull Transaction rack) {
    val entity = repository.findOne(rack.getId());
    mapper.pass(mapper.jpa(rack), entity);
    repository.save(entity);
  }

}
