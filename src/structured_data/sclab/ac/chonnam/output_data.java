package structured_data.sclab.ac.chonnam;

public class output_data {
public	String proj;
public		String project;
public		String User;
public		String Date_project;
public		String title;
public		String description;
public		String[] tag;
public		String status;

public		long initialTime =0;
public		long endTime=0;
public		double div;
public		double concern ;
public		int no_supporter;
public		int no_follower;
public		int no_expert_comment;
	
	public output_data(String proj, String project, String user, String date_project, String title, String description,
			String[] tag, long initialTime, long endTime, double div, double concern, int no_supporter, int no_follower,
			int no_expert_comment) {
		super();
		this.proj = proj;
		this.project = project;
		User = user;
		Date_project = date_project;
		this.title = title;
		this.description = description;
		this.tag = tag;
		this.initialTime = initialTime;
		this.endTime = endTime;
		this.div = div;
		this.concern = concern;
		this.no_supporter = no_supporter;
		this.no_follower = no_follower;
		this.no_expert_comment = no_expert_comment;
	}

	public output_data() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getProj() {
		return proj;
	}

	public void setProj(String proj) {
		this.proj = proj;
	}
   
	public String getProject() {
		return project;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
   
	public String getStatus() {
		return status;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getUser() {
		return User;
	}

	public void setUser(String user) {
		User = user;
	}

	public String getDate_project() {
		return Date_project;
	}

	public void setDate_project(String date_project) {
		Date_project = date_project;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String[] getTag() {
		return tag;
	}

	public void setTag(String[] tag) {
		this.tag = tag;
	}

	public long getInitialTime() {
		return initialTime;
	}

	public void setInitialTime(long initialTime) {
		this.initialTime = initialTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public double getDiv() {
		return div;
	}

	public void setDiv(double div) {
		this.div = div;
	}

	public double getConcern() {
		return concern;
	}

	public void setConcern(double concern) {
		this.concern = concern;
	}

	public int getNo_supporter() {
		return no_supporter;
	}

	public void setNo_supporter(int no_supporter) {
		this.no_supporter = no_supporter;
	}

	public int getNo_follower() {
		return no_follower;
	}

	public void setNo_follower(int no_follower) {
		this.no_follower = no_follower;
	}

	public int getNo_expert_comment() {
		return no_expert_comment;
	}

	public void setNo_expert_comment(int no_expert_comment) {
		this.no_expert_comment = no_expert_comment;
	}
	
	
	
}
