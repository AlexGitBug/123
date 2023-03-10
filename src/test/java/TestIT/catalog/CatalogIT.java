package TestIT.catalog;

import dao.Dao;
import entity.Catalog;
import lombok.Cleanup;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.SessionUtil;

import java.io.Serializable;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static util.SessionUtil.closeTransactionSession;
import static util.SessionUtil.openTransactionSession;

public class CatalogIT{

    @Test
    void checkCatalogSaveTestIT() {
        try (Session session = openTransactionSession()) {
            Catalog tv = Catalog.builder().category("TV").build();

            session.save(tv);

            closeTransactionSession();

            Catalog actualResult = session.get(Catalog.class, tv.getId());
            assertNotNull(actualResult.getId());
        }
    }

    @Test
    void checkCatalogFindByIdTestIT() {
        try (Session session = openTransactionSession()) {
            Catalog tv = Catalog.builder().category("TV").build();

            session.save(tv);
            closeTransactionSession();

            Catalog actualResult = session.get(Catalog.class, tv.getId());
            assertThat(actualResult).isEqualTo(tv);
        }
    }

    @Test
    void checkCatalogDeleteTestIT() {
        try (Session session = openTransactionSession()) {
            Catalog tv = Catalog.builder().category("TV").build();

            session.save(tv);
            closeTransactionSession();


        }
    }
}