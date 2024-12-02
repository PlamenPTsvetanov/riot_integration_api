package me.riot.integration.api.icon;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import me.riot.integration.api._common.datamodel.BaseDTO;
import me.riot.integration.api._common.datamodel.BaseOrmBean;
import me.riot.integration.api._common.services.BaseService;
import me.riot.integration.api._common.utils.HTTPMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Slf4j
@Service
public class IconService extends BaseService {
    private static final String ICON_END_POINT = "img/profileicon/";
    private static final String ICON_EXTENSION = ".png";

    private final IIconRepository repository;

    public IconService(IIconRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public byte[] getSummonerIcon(BigInteger iconId) {
        byte[] icon;
        try {
            IconOrmBean iconOrm = this.repository.getByRiotIdAndType(iconId.intValue(), IconType.SUMMONER.toString());
            if (iconOrm != null) {
                log.info("Icon found in local database.");
                return iconOrm.getImage();
            } else {
                log.info("Fetching new data.");
                String modifiedRequest = _dataDragonUrl +
                        ICON_END_POINT +
                        iconId +
                        ICON_EXTENSION;

                icon = super.sendRequestBytes(modifiedRequest, HTTPMethod.GET);

                IconOrmBean orm = new IconOrmBean();
                orm.setImage(icon);
                orm.setType(IconType.SUMMONER.toString());
                orm.setRiotId(iconId.intValue());
                this.repository.save(orm);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return icon;
    }

    @Override
    protected JpaRepository getRepository() {
        return this.repository;
    }

    @Override
    protected BaseOrmBean build(BaseDTO dto) {
        return null;
    }
}
