package org.darkness.jbpmtest;

import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;

/**
 * @author Darkness
 * 
 * QQ: 893951837 Email: darkness_sky@qq.com Blog:
 * http://depravedAngel.javaeye.com/
 * 
 * Copyright (c) 2009 by Darkness
 * 
 * @date Apr 18, 2009 9:20:27 AM
 * @version 1.0
 */
public class JbpmTest extends TestCase {

	static JbpmConfiguration jbpmConfiguration = JbpmConfiguration
			.getInstance();

	// Jbpm_01_CreateTable 创建表格
	public void testCreateTable() {

		jbpmConfiguration.createSchema();
	}

	// Jbpm_02_DeployProcessDefinition
	public void testDeployProcessDefinition() {

		ProcessDefinition processDefinition = ProcessDefinition
				.parseXmlResource("process.xml");

		// context对象类似于hibernate session对象的功能
		JbpmContext context = jbpmConfiguration.createJbpmContext();

		try {
			context.deployProcessDefinition(processDefinition);
		} finally {
			// context对象需要关闭
			context.close();
		}

	}

	// Jbpm_03_CreateDocument
	public void testCreateDocument() {

		Session session = HibernateUtils.getSession();

		try {
			session.beginTransaction();

			Document doc = new Document();
			doc.setTitle("测试公文");
			doc.setContent("测试公文的内容");
			doc.setCreator("赵一");

			session.save(doc);

			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
		} finally {
			HibernateUtils.closeSession(session);
		}
	}

	// Jbpm_04_CreateProcessInstance
	// //创建流程实例，并将流程实例与公文互相绑定
	public void testCreateProcessInstance() {

		JbpmContext context = jbpmConfiguration.createJbpmContext();
		context.setSessionFactory(HibernateUtils.getSessionFactory());

		// 从数据库中加载ProcessDefinition对象
		ProcessDefinition definition = context.getGraphSession()
				.findLatestProcessDefinition("文档测试流程");

		// 从流程中创建一个流程实例
		ProcessInstance processInstance = new ProcessInstance(definition);

		// 存储流程实例
		context.save(processInstance);

		// 加载公文
		Document doc = (Document) context.getSession().load(Document.class, 1);

		// 绑定流程实例到公文
		doc.setProcessIntanceId(processInstance.getId());

		// 绑定公文到流程实例(ContextInstance相当于一个变量的容器)
		processInstance.getContextInstance().createVariable("document",
				doc.getId());

		context.close();
	}

	// Jbpm_05_SubmitDocument
	public void testSubmitDocument() {

		JbpmContext context = jbpmConfiguration.createJbpmContext();
		context.setSessionFactory(HibernateUtils.getSessionFactory());

		Document doc = (Document) context.getSession().load(Document.class, 1);

		ProcessInstance processInstance = context.getProcessInstance(doc
				.getProcessIntanceId());

		// 触发流程实例走向下一步
		processInstance.getRootToken().signal();

		context.close();
	}

	// //查看文档的当前位置Jbpm_06_CurrentNode
	public void testCurrentNode() {

		JbpmContext context = jbpmConfiguration.createJbpmContext();
		context.setSessionFactory(HibernateUtils.getSessionFactory());

		Document doc = (Document) context.getSession().load(Document.class, 1);

		ProcessInstance processInstance = context.getProcessInstance(doc
				.getProcessIntanceId());

		// 当前节点？
		System.err.println(processInstance.getRootToken().getNode().getName());

		context.close();
	}

	// Jbpm_07_SearchMyTaskList
	@SuppressWarnings("unchecked")
	public void testSearchMyTaskList() {

		JbpmContext context = jbpmConfiguration.createJbpmContext();
		context.setSessionFactory(HibernateUtils.getSessionFactory());

		searchTaskList("张三", context);
		searchTaskList("李四", context);
		searchTaskList("王五", context);

		context.close();
	}

	@SuppressWarnings("unchecked")
	public List searchTaskList(String name, JbpmContext context) {

		List tasks = context.getTaskMgmtSession().findTaskInstances(name);

		System.err.println(name + "的文档有：");
		for (Iterator iter = tasks.iterator(); iter.hasNext();) {
			TaskInstance taskInstance = (TaskInstance) iter.next();
			Integer docId = (Integer) taskInstance.getProcessInstance()
					.getContextInstance().getVariable("document");
			System.err.println(docId);
		}

		return tasks;
	}

	// Jbpm_08_NextNode
	@SuppressWarnings("unchecked")
	public void testFlowNextNode() {

		JbpmContext context = jbpmConfiguration.createJbpmContext();
		context.setSessionFactory(HibernateUtils.getSessionFactory());

		// 张三审批
		List tasks = searchTaskList("张三", context);
		flowNextNode(tasks);// 依次对张三的公文进行提交

		// 李四审批
		tasks = searchTaskList("李四", context);
		flowNextNode(tasks);// 依次对李四的公文进行提交
		
		// 王五审批
		tasks = searchTaskList("王五", context);
		flowNextNode(tasks);// 依次对王五的公文进行提交
		
		context.close();
	}

	// 依次对公文进行提交
	public void flowNextNode(List<TaskInstance> tasks) {

		for (Iterator<TaskInstance> iter = tasks.iterator(); iter.hasNext();) {
			TaskInstance taskInstance = iter.next();

			// 已经审批结束，继续提交，这将触发流程继续向下流动！
			taskInstance.end();

			Integer docId = (Integer) taskInstance.getProcessInstance()
					.getContextInstance().getVariable("document");
			System.err.println(docId + "已被审批完成");
		}
	}

	//测试某个流程实例是否已经结束Jbpm_09_ProcessInstanceIsEnded
	public void testHasEnded(){
		
		JbpmContext context = jbpmConfiguration.createJbpmContext();
		context.setSessionFactory(HibernateUtils.getSessionFactory());
		
		Document doc = (Document)context.getSession().load(Document.class, 1);
		
		ProcessInstance processInstance = context.getProcessInstance(doc.getProcessIntanceId());
		
		System.err.println("流程已结束？ - "+processInstance.hasEnded());
		
		context.close();
	}
}
