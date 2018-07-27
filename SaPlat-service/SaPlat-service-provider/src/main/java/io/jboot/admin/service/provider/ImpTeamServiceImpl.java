package io.jboot.admin.service.provider;

import io.jboot.admin.service.api.ImpTeamService;
import io.jboot.admin.service.entity.model.ExpertGroup;
import io.jboot.admin.service.entity.model.ImpTeam;
import io.jboot.aop.annotation.Bean;
import io.jboot.core.rpc.annotation.JbootrpcService;
import io.jboot.db.model.Column;
import io.jboot.db.model.Columns;
import io.jboot.service.JbootServiceBase;

import javax.inject.Singleton;
import java.util.List;

@Bean
@Singleton
@JbootrpcService
public class ImpTeamServiceImpl extends JbootServiceBase<ImpTeam> implements ImpTeamService {
    @Override
    public List<ImpTeam> findByUserID(Long id){
        Columns columns = Columns.create();
        columns.eq("createUserID",id);
        return DAO.findListByColumns(columns);
    }
    @Override
    public ImpTeam findByUserIDAndProjectID(Long userId,Long projectId){
        Columns columns = Columns.create();
        columns.eq("createUserID",userId);
        columns.eq("projectID",projectId);
        return DAO.findFirstByColumns(columns);
    }

    @Override
    public ImpTeam findByProjectId(Long id) {
        return DAO.findFirstByColumn(Column.create("projectID",id));
    }

    @Override
    public List<ImpTeam> findByExpertGroup(ExpertGroup... expertGroups) {
        Columns columns = Columns.create();
        for (ExpertGroup expertGroup : expertGroups) {
            columns.like("expertGroupIDs", "%" + expertGroup.getId() +"%");
        }
        return DAO.findListByColumns(columns);
    }
}