import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    List<Shop> findByName(String name);
    List<Shop> findByMerchant(Merchant merchant);
}