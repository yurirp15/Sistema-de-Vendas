package github.com.yurirp15.domain.repository;

import github.com.yurirp15.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Produtos extends JpaRepository<Produto, Integer> {
}
