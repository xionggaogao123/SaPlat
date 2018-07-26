package io.jboot.admin.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.GET;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Page;
import io.jboot.admin.base.common.RestResult;
import io.jboot.admin.base.exception.BusinessException;
import io.jboot.admin.base.interceptor.NotNullPara;
import io.jboot.admin.base.rest.datatable.DataTable;
import io.jboot.admin.base.web.base.BaseController;
import io.jboot.admin.service.api.*;
import io.jboot.admin.service.entity.model.*;
import io.jboot.admin.service.entity.status.system.ProjectUndertakeStatus;
import io.jboot.admin.support.auth.AuthUtils;
import io.jboot.core.rpc.annotation.JbootrpcService;
import io.jboot.web.controller.annotation.RequestMapping;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RequestMapping("/app/projectUndertake")
public class ProjectUndertakeController extends BaseController {

    @JbootrpcService
    private ProjectService projectService;

    @JbootrpcService
    private ProjectUndertakeService projectUndertakeService;

    @JbootrpcService
    private FacAgencyService facAgencyService;

    @JbootrpcService
    private OrganizationService organizationService;

    @JbootrpcService
    private StructPersonLinkService structPersonLinkService;

    @JbootrpcService
    private PersonService personService;

    @JbootrpcService
    private OrgStructureService orgStructureService;

    @JbootrpcService
    private ImpTeamService impTeamService;

    /**
     * 跳转榜单页面
     */
    public void toProjectList() {
        render("projectList.html");
    }

    /**
     * 榜单显示已发布的项目
     */
    public void projectList() {
        int pageNumber = getParaToInt("pageNumber", 1);
        int pageSize = getParaToInt("pageSize", 30);
        Project project = new Project();
        project.setIsPublic(true);
        Page<Project> page = projectService.findPageByIsPublic(project, pageNumber, pageSize);
        renderJson(new DataTable<Project>(page));
    }

    /**
     * 查看项目详细信息
     */
    @NotNullPara({"id"})
    public void see() {
        User user = AuthUtils.getLoginUser();
        Long id = getParaToLong("id");
        Project model = projectService.findById(id);
        setAttr("model", model).render("see.html");
    }

    /**
     * 介入项目
     */
    @NotNullPara({"id"})
    @RequiresRoles(value = {"组织机构", "服务机构"}, logical = Logical.AND)
    public void agency() {
        Long id = getParaToLong("id");
        User user = AuthUtils.getLoginUser();
        Organization organization = organizationService.findById(user.getUserID());
        FacAgency facAgency = facAgencyService.findByOrgId(organization.getId());

        ProjectUndertake projectUndertake = projectUndertakeService.findByProjectIdAndFacAgencyId(id, facAgency.getId());
        Project project = projectService.findById(id);
        if (projectUndertake == null) {
            projectUndertake = new ProjectUndertake();
            projectUndertake.setCreateUserID(user.getId());
            projectUndertake.setCreateTime(new Date());
            projectUndertake.setProjectID(id);
            projectUndertake.setFacAgencyID(facAgency.getId());
        } else if (projectUndertake.getStatus() == 0) {
            renderJson(RestResult.buildError("您已经申请过了，请不要重复申请！"));
            throw new BusinessException("您已经申请过了，请不要重复申请！");
        }
        projectUndertake.setName(project.getName());
        projectUndertake.setDeadTime(project.getEndPublicTime());
        projectUndertake.setApplyOrInvite(false);
        projectUndertake.setStatus(0);
        projectUndertake.setReply(null);
        projectUndertake.setLastAccessTime(new Date());
        projectUndertake.setLastUpdateUserID(user.getId());
        projectUndertake.setRemark(null);
        projectUndertake.setIsEnable(true);

        Notification notification = new Notification();
        notification.setName("项目承接通知");
        notification.setSource("/app/projectAndServiceOrg");
        notification.setContent("您好，" + organization.getName() + "希望承接您的项目委评，请及时处理！");
        //TODO 待处理接受模块路径
        notification.setRecModule("");
        notification.setReceiverID(Math.toIntExact(project.getUserId()));
        notification.setCreateTime(new Date());
        notification.setCreateUserID(user.getId());
        notification.setLastAccessTime(new Date());
        notification.setLastUpdateUserID(user.getId());
        notification.setStatus(Integer.valueOf(ProjectUndertakeStatus.WAITING));
        notification.setIsEnable(true);

        if (!projectUndertakeService.saveOrUpdateAndSend(projectUndertake, notification)) {
            renderJson(RestResult.buildError("申请介入失败，请重新尝试。"));
            throw new BusinessException("申请介入失败，请重新尝试。");
        }
        renderJson(RestResult.buildSuccess());
    }

    /**
     * 加载承接详情页面
     */
    public void projectUndertakeIndex() {
        render("projectUndertake.html");
    }

    /**
     * 承接详情页面数据加载，
     * 参数 applyOrInvite ：
     * true    主动申请
     * false   被邀请
     * 参数 flag ：
     * true    作为请求发起方
     * false   作为请求接受方
     */
    public void projectUndertakeList() {
        User user = AuthUtils.getLoginUser();
        Boolean applyOrInvite = getParaToBoolean("applyOrInvite");
        Boolean flag = getParaToBoolean("flag");
        int pageNumber = getParaToInt("pageNumber", 1);
        int pageSize = getParaToInt("pageSize", 30);
        ProjectUndertake projectUndertake = new ProjectUndertake();
        projectUndertake.setApplyOrInvite(applyOrInvite);
        projectUndertake.setIsEnable(true);
        if (flag) {
            projectUndertake.setCreateUserID(user.getId());
        } else {
            projectUndertake.setFacAgencyID(facAgencyService.findByOrgId(organizationService.findById(user.getUserID()).getId()).getId());
        }
        Page<ProjectUndertake> page = projectUndertakeService.findPage(projectUndertake, pageNumber, pageSize);
        renderJson(new DataTable<ProjectUndertake>(page));
    }

    /**
     * 处理邀请请求，
     * 参数 invite：
     * 1   拒绝
     * 2   同意
     * id：承接的 id
     * reply: 拒绝时回显
     */
    public void invite() {
        Integer invite = getParaToInt("invite");
        Long id = getParaToLong("id");
        if (invite == null || id == null || (!invite.equals(Integer.valueOf(ProjectUndertakeStatus.REFUSE))
                && !invite.equals(Integer.valueOf(ProjectUndertakeStatus.ACCEPT)))) {
            renderJson(RestResult.buildError("请求参数错误"));
            throw new BusinessException("请求参数错误");
        }
        ProjectUndertake projectUndertake = projectUndertakeService.findById(id);
        if (projectUndertake == null || !projectUndertake.getIsEnable()) {
            renderJson(RestResult.buildError("请求参数错误"));
            throw new BusinessException("请求参数错误");
        }
        User user = AuthUtils.getLoginUser();
        String reply = null;
        Notification notification = new Notification();
        if (invite.equals(Integer.valueOf(ProjectUndertakeStatus.REFUSE))) {
            reply = getPara("reply");
            notification.setName("邀请拒绝通知");
            notification.setContent(user.getName() + "已拒绝您的邀请！");
            projectUndertake.setStatus(Integer.valueOf(ProjectUndertakeStatus.REFUSE));
        }
        if (invite.equals(Integer.valueOf(ProjectUndertakeStatus.ACCEPT))) {
            notification.setName("邀请接受通知");
            notification.setContent(user.getName() + "已接受您的邀请！");
            projectUndertake.setStatus(Integer.valueOf(ProjectUndertakeStatus.ACCEPT));
        }
        projectUndertake.setReply(reply);
        projectUndertake.setLastUpdateUserID(user.getId());
        projectUndertake.setLastAccessTime(new Date());

        Long projectID = projectUndertake.getProjectID();
        Long receiverID = projectService.findById(projectID).getUserId();
        notification.setSource("/app/projectUndertake/invite");
        //TODO 等待书写接受模块
        notification.setRecModule("");
        notification.setReceiverID(Math.toIntExact(receiverID));
        notification.setCreateUserID(user.getId());
        notification.setCreateTime(new Date());
        notification.setLastUpdateUserID(user.getId());
        notification.setLastAccessTime(new Date());
        notification.setIsEnable(true);
        notification.setStatus(0);

        if (!projectUndertakeService.saveOrUpdateAndSend(projectUndertake, notification)) {
            renderJson(RestResult.buildError("请求失败，请重新尝试！"));
            throw new BusinessException("请求失败，请重新尝试！");
        }
        renderJson(RestResult.buildSuccess());
    }

    /**
     * 去重
     */
    static String sub(String str) {
        List list = new ArrayList();
        StringBuffer sb = new StringBuffer(str);
        int j = 0;
        for (int i = 0; i < str.length(); i++) {
            if (list.contains(str.charAt(i))) {
                sb.deleteCharAt(i - j);
                j++;
            } else {
                list.add(str.charAt(i));
            }
        }
        return sb.toString();
    }

    /**
     * 实施小组建立初始页面(自评)
     */
    public void toSelfProjectImpTeam() {
        StringBuilder string = new StringBuilder();
        User user = AuthUtils.getLoginUser();
        List<Project> projects = projectService.findAll();
        List<StructPersonLink> structPersonLinks = structPersonLinkService.findAll();
        List<Project> selfProjects = new ArrayList<>();
        List<OrgStructure> orgStructures = new ArrayList<>();
        //自评的所有项目
        for (Project project : projects) {
            if (project.getUserId().equals(user.getId()) && project.getAssessmentMode().equals("自评")) {
                selfProjects.add(project);
            }
        }
        for (StructPersonLink structPersonLink : structPersonLinks) {
            string.append(structPersonLink.getStructID());
        }
        for (int i = 0; i < sub(string.toString()).length(); i++) {
            orgStructures.add(orgStructureService.findById(Character.getNumericValue(sub(string.toString()).charAt(i))));
        }
        setAttr("selfProjects", selfProjects).setAttr("orgStructures", orgStructures).render("selfProjectImpTeam.html");
    }

    /**
     * 下拉框二级联动
     */
    @Before(GET.class)
    public void persons() {
        List<StructPersonLink> structPersonLinks = structPersonLinkService.findByStructId(getParaToLong("orgStructureId"));
        List<Person> persons = new ArrayList<>();
        for (StructPersonLink structPersonLink : structPersonLinks) {
            persons.add(personService.findById(structPersonLink.getPersonID()));
        }
        JSONObject json = new JSONObject();
        json.put("persons", persons);
        renderJson(json);
    }


    /**
     * 实施小组建立初始页面(委评)
     */
    public void toOtherProjectImpTeam() {
        StringBuilder string = new StringBuilder();
        List<ProjectUndertake> projectUndertakes = projectUndertakeService.findAll();
        List<StructPersonLink> structPersonLinks = structPersonLinkService.findAll();
        List<ProjectUndertake> otherProjects = new ArrayList<>();
        List<OrgStructure> orgStructures = new ArrayList<>();
        //委评且同意介入或同意邀请的项目
        for (ProjectUndertake projectUndertake : projectUndertakes) {
            if (projectUndertake.getStatus() == 2) {//不管是邀请时服务机构同意还是申请时项目自身同意，这里只做一个判断即可
                otherProjects.add(projectUndertake);
            }
        }
        for (StructPersonLink structPersonLink : structPersonLinks) {
            string.append(structPersonLink.getStructID());
        }
        for (int i = 0; i < sub(string.toString()).length(); i++) {
            orgStructures.add(orgStructureService.findById(Character.getNumericValue(sub(string.toString()).charAt(i))));
        }
        setAttr("otherProjects", otherProjects).setAttr("orgStructures", orgStructures).render("otherProjectImpTeam.html");
    }

    /**
     * 实施小组提交资料
     */
    @Before(POST.class)
    public void ImpTeam() {
        User loginUser = AuthUtils.getLoginUser();
        ImpTeam impTeam = getBean(ImpTeam.class, "impTeam");
        impTeam.setCreateTime(new Date());
        impTeam.setLastAccessTime(new Date());
        impTeam.setCreateUserID(loginUser.getId());
        impTeam.setLastUpdateUserID(loginUser.getId());
        if (impTeamService.saveOrUpdate(impTeam)) {
            renderJson(RestResult.buildSuccess("立项成功"));
        } else {
            renderJson(RestResult.buildError("立项失败"));
            throw new BusinessException("立项失败");
        }
    }
}
