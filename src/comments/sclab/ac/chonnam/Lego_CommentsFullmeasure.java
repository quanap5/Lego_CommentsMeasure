package comments.sclab.ac.chonnam;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import structured_data.sclab.ac.chonnam.output_data;

public class Lego_CommentsFullmeasure {
	String[] project;

	public Lego_CommentsFullmeasure() {
		super();

	}

	static String User;
	static String Date_project;
	static String title;
	static String description;
	static String[] tag;
	static String status;

	static long initialTime = 0;
	static long endTime = 0;
	static double div;
	static double concern;
	static int no_supporter;
	static int no_follower;
	static String[] prefixes = { "k", "M", "G" };
	static int no_expert_comment;

	/**
	 * Read word from text file
	 * 
	 * @return ArrayList<String>
	 * @see loadWordsList
	 */
	public ArrayList<String> loadWordsList(String pLoc) {
		ArrayList<String> wordlist = new ArrayList<>();
		try {
			ArrayList<String> words = (ArrayList<String>) Files.readAllLines(Paths.get(pLoc), StandardCharsets.UTF_8);
			for (String string : words) {
				wordlist.add(string.trim());

			}

		} catch (IOException ex) {
			// System.out.println("Load Word error");
		}
		return wordlist;
	}

	public static output_data trainingData(String proj, String project) throws Exception {
		// Lego_CommentsFullmeasure list_project = new
		// Lego_CommentsFullmeasure();
		// ArrayList<String> id_list = new ArrayList<String>();
		// id_list =
		// list_project.loadWordsList("C:/Users/Quan/workspace/Lego_CommentsMeasure/wordlist/input_project");
		// String[] id_project = new String[id_list.size()];
		// id_project = id_list.toArray(id_project);
		//
		// ArrayList<output_data> Training_Data = new ArrayList<output_data>();
		// for (int i = 0; i < id_project.length; i=i+2) {
		//
		// }
		//
		output_data object1 = new output_data();
		// String proj = id_project[0];
		// String project = id_project[1];

		String limit = "101";
		String url1 = "https://ideas.lego.com/comments/project/" + project
				+ "/comments?order=oldest&from=min&style=children&max_id=&min_id=&limit=" + limit
				+ "&id=comments-query-/comments" + "/project/" + project + "/comments";
		ArrayList<String> leader = extractLeaderuser();
		String next_index = "";
		int reply = 0;
		int comment = 0;
		ArrayList<MyComments> comments_Data = new ArrayList<MyComments>();
		HashSet noDupSet = new HashSet();
		ArrayList<String> List_Id = new ArrayList<String>();
		ArrayList<String> List_name = new ArrayList<String>();
		while (true) {

			String url = url1 + next_index;
			String json = IOUtils.toString(new URL(url));
			Gson gson = new GsonBuilder().create();
			MyPojo myPojo = gson.fromJson(json, MyPojo.class);

			if (myPojo.getResults().length == 0) {
				break;
			}
			if (next_index == "") {
				initialTime = Long.parseLong(myPojo.getResults()[0].getCreated_at());
				endTime = initialTime;
			}

			for (int i = 0; i < myPojo.getResults().length; i++) {
				MyComments myData1 = new MyComments(myPojo.getResults()[i]);
				comments_Data.add(myData1);

				// System.out.println("======" + comment + "==========");
				// System.out.print("Author: ");
				List_name.add(myPojo.getResults()[i].getAuthor().getAlias());
				// System.out.println(myPojo.getResults()[i].getAuthor().getAlias());
				// System.out.print("Author_id: ");
				noDupSet.add(myPojo.getResults()[i].getAuthor().getId());
				List_Id.add(myPojo.getResults()[i].getAuthor().getId());
				// System.out.println(myPojo.getResults()[i].getAuthor().getId());
				// System.out.print("Comment: ");
				// System.out.println(myPojo.getResults()[i].getComment());
				// System.out.print("Created_at: ");
				if (Long.parseLong(myPojo.getResults()[i].getCreated_at()) >= endTime) {
					endTime = Long.parseLong(myPojo.getResults()[i].getCreated_at());
				}

				// System.out.println(myPojo.getResults()[i].getCreated_at());
				// System.out.print("User_vote: ");
				// System.out.println(myPojo.getResults()[i].getUser_vote().getCount());
				// System.out.print("Replies_count: ");
				// System.out.println(myPojo.getResults()[i].getReplies_count());
				reply = reply + Integer.parseInt(myPojo.getResults()[i].getReplies_count());
				comment = comment + 1;
				// System.out.print(myPojo.getResults()[i].getUser_vote().getDistribution().get_minus1());
				// System.out.print(myPojo.getResults()[i].getUser_vote().getDistribution().get_zero0());
				// System.out.println(myPojo.getResults()[i].getUser_vote().getDistribution().get_plus1());
				if (myPojo.getResults()[i].getReplies_count().equals("0") == false) {
					// extract reply
					String limit_reply = "10";
					String url_reply1 = "https://ideas.lego.com/comments/project/" + project + "/comments/"
							+ myPojo.getResults()[i].getId() + "/replies?order=oldest&from=min&style=flat"
							+ "&max_id=&min_id=&limit=21&id=comments-query-/comments/project/" + project + "/comments";

					String next_index_reply = "";

					// ArrayList<MyReply> replys_Data = new
					// ArrayList<MyReply>();

					while (true) {

						String url_reply = url_reply1 + next_index_reply;
						// System.out.println(url_reply);
						String json_reply = IOUtils.toString(new URL(url_reply));
						Gson gson_reply = new GsonBuilder().create();
						MyPojo myPojo_reply = gson_reply.fromJson(json_reply, MyPojo.class);

						if (myPojo_reply.getResults().length == 0) {
							break;
						}

						for (int j = 0; j < myPojo_reply.getResults().length; j++) {
							MyComments myData_reply1 = new MyComments(myPojo_reply.getResults()[j]);
							comments_Data.add(myData_reply1);

							// System.out.println("======" + comment +
							// "==========");
							// System.out.print("AuthorReply: ");
							List_name.add(myPojo.getResults()[i].getAuthor().getAlias());
							// System.out.println(myPojo_reply.getResults()[j].getAuthor().getAlias());
							// System.out.print("Author_idReply: ");
							noDupSet.add(myPojo_reply.getResults()[j].getAuthor().getId());
							List_Id.add(myPojo.getResults()[i].getAuthor().getId());
							// System.out.println(myPojo_reply.getResults()[j].getAuthor().getId());
							// System.out.print("CommentReply: ");
							// System.out.println(myPojo_reply.getResults()[j].getComment());
							// System.out.print("Created_atReply: ");
							if (Long.parseLong(myPojo.getResults()[j].getCreated_at()) >= endTime) {
								endTime = Long.parseLong(myPojo.getResults()[j].getCreated_at());
							}
							// System.out.println(myPojo_reply.getResults()[j].getCreated_at());
							// System.out.print("User_voteReply: ");
							// System.out.println(myPojo_reply.getResults()[j].getUser_vote().getCount());
							// System.out.print("Replies_countReply: ");
							// System.out.println(myPojo_reply.getResults()[j].getReplies_count());
							comment = comment + 1;
							System.out
									.print(myPojo_reply.getResults()[j].getUser_vote().getDistribution().get_minus1());
							// System.out.print(myPojo_reply.getResults()[j].getUser_vote().getDistribution().get_zero0());
							System.out
									.println(myPojo_reply.getResults()[j].getUser_vote().getDistribution().get_plus1());

						}

						next_index_reply = "&min_id="
								+ myPojo_reply.getResults()[myPojo_reply.getResults().length - 1].getId();

					}
				}
			}

			next_index = "&min_id=" + myPojo.getResults()[myPojo.getResults().length - 1].getId();
			// System.out.println("Reply + Comment=" + reply + " + " + comment +
			// " = " + (reply + comment));

		}

		Gson gson1 = new Gson();
		String json1 = gson1.toJson(comments_Data);
		// System.out.println(json1);
		// Date and current time
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
		Calendar cal = Calendar.getInstance();
		String mynamefile = "CommentsLego" + project + dateFormat.format(cal.getTime()) + ".txt";
		// String
		// mynamefileTotal="InfluenceTotal_quirky"+dateFormat.format(cal.getTime())+".txt";
		try {
			FileWriter writer = new FileWriter(mynamefile);
			// writer.write(json1);
			// writer.close();
			// System.out.println("Write file: Success");
			// System.out.print("Number of noDulicate Author: ");
			// System.out.println(noDupSet.size());

			// Set<String> unique = new HashSet<String>();
			Set<String> unique = new HashSet<String>(List_Id);
			// int Sum_id = 0;
			// for (String key : unique) {
			// //System.out.println(key + ": " + Collections.frequency(List_Id,
			// key));
			// Sum_id= Sum_id +Collections.frequency(List_Id, key);
			// }
			// //System.out.println(Sum_id);

		} catch (Exception e) {
			// TODO: handle exception

		}

		for (int i = 0; i < leader.size(); i++) {
			for (int k = 0; k < List_name.size(); k++) {
				if (leader.get(i) == List_name.get(k)) {
					no_expert_comment = no_expert_comment + 1;
				}

			}

		}
		div = diversityDegree(List_Id);
		// System.out.println("Interest measure==> DIV: " + div);
		description(proj);
		// System.out.println("first Comment at: " + initialTime);
		// System.out.println("last comment at: " + endTime);
		// System.out.println("About " + (double) (endTime - initialTime) / (24
		// * 3600) + "days");
		// System.out.println("=======INTEREST MEASUREMENT======");
		concern = concernDegree(noDupSet.size(), initialTime, endTime);
		// //System.out.println("Interest measure==> noSUPPORTER:
		// "+no_supporter);
		// //System.out.println("Interest measure==> noFOLLOWER: "+no_follower);
		// //System.out.println("Interest measure==> DIV: "+div);
		// //System.out.println("Interest measure==> CONCERN: "+concern);
		// //System.out.println("Interest measure==> EXPERT comment:
		// "+no_expert_comment);
		// -----------------
		object1.setProj(proj);
		object1.setProject(project);
		object1.setTitle(title);
		object1.setDescription(description);
		object1.setTag(tag);
		// object1.setTag();
		object1.setUser(User);
		object1.setDate_project(Date_project);
		object1.setNo_supporter(no_supporter);
		object1.setNo_follower(no_follower);
		object1.setStatus(status);
		object1.setDiv(div);
		object1.setConcern(concern);
		object1.setNo_expert_comment(no_expert_comment);

		// System.out.println("Name user: " + object1.User);
		// System.out.println("Title: " + object1.title);
		// System.out.println("Description: " + object1.description);
		// System.out.println("Tags: " );
		for (int i = 0; i < object1.tag.length; i++) {
			// System.out.println(object1.tag[i]);

		}
		// System.out.println("Date publish: " + object1.Date_project);
		// System.out.println("Status: " + object1.status);

		// System.out.println("Interest measure==> noSUPPORTER: " +
		// object1.no_supporter);
		// System.out.println("Interest measure==> noFOLLOWER: " +
		// object1.no_follower);
		// System.out.println("Interest measure==> DIV: " + object1.div);
		// System.out.println("Interest measure==> CONCERN: " +
		// object1.concern);
		// System.out.println("Interest measure==> EXPERT comment: " +
		// object1.no_expert_comment);

		return object1;

	}

	public static void description(String proj) throws Exception {
		// String proj="101885";
		Document doc_des = Jsoup.connect("https://ideas.lego.com/projects/" + proj).get();

		Elements title = doc_des.select("hgroup.project-title");
		// System.out.print("TITLE: ");
		// System.out.println(title.first().text());
		Lego_CommentsFullmeasure.title = title.first().text();

		Elements description = doc_des.select("section.project-description");
		// System.out.println("DESCRIPTION:");
		// System.out.println(description.text());
		Lego_CommentsFullmeasure.description = description.text();

		Elements tags = doc_des.select("section.project-tags");
		Elements tags_list = tags.select("li");
		// System.out.println("TAGS:");
		ArrayList<String> tag_add = new ArrayList<>();
		for (final Element li : tags_list) {
			//// System.out.println(li.text());
			tag_add.add(li.text());

		}
		tag = new String[tag_add.size()];
		tag = tag_add.toArray(tag);

		// System.out.println("====Stats of Projects====");
		Elements profile_project = doc_des.select("section.tile.project-support-tile");
		Elements tile_progression = profile_project.select("article.tile-progression");
		Elements tags_supporters = tile_progression.select("li.tile-supporters");
		// System.out.print("no Supporter: ");
		// System.out.println(tags_supporters.select("h3").text());
		if (tags_supporters.select("h3").text().equals("") == false) {
			no_supporter = Integer.parseInt(tags_supporters.select("h3").text());
		} else {
			Elements tags_supporters2 = tile_progression.select("section.span8");
			// no_supporter =
			// Integer.parseInt(tags_supporters.select("h3").text());
			// System.out.print("no Supporter: ");
			// System.out.println(tags_supporters2.select("h2").text());
			// String
			// idea=tags_supporters2.select("h2").text().replaceAll("\\p{Punct}+",
			// " ");
			no_supporter = Integer.parseInt(tags_supporters2.select("h2").text().replaceAll("\\p{Punct}+", ""));
		}

		Elements tags_days = tile_progression.select("li.tile-days-remaining");
		// System.out.print("no Day remaining: ");
		// System.out.println(tags_days.select("h3").text());
		Elements project_state = profile_project.select("section.project-state");
		// System.out.print("State of Project1: ");
		// System.out.println(project_state.select("span").text());
		if (project_state.select("span").text().equals("") == false) {
			status = project_state.select("span").text();
		} else {
			Elements profile_progression = doc_des.select("section.users-project-progression");
			Elements profile_progression_description0 = profile_progression.select("li.project-step-progression");
			Elements profile_progression_description = profile_progression_description0
					.select("article.progress-meter-description");
			// System.out.print("State of Project2: ");
			// System.out.println(profile_progression_description.select("h2").text());
			// String
			// idea=tags_supporters2.select("h2").text().replaceAll("\\p{Punct}+",
			// " ");
			status = profile_progression_description.select("h2").text();
		}

		Elements tags_first = profile_project.select("li.tile-stats.first");
		// System.out.print("no View: ");
		// System.out.println(tags_first.select("span").text());
		Elements tags_comment = profile_project.select("li.tile-stats");
		// System.out.print("no Comment: ");
		// System.out.println(tags_comment.select("span").text());
		Elements tags_follower = profile_project.select("li.tile-stats.followers.last");
		// System.out.print("no Follower project: ");
		no_follower = (int) parse(tags_follower.select("span").text());// Nen
																		// chu y
																		// sua
																		// lai
		// System.out.println(tags_follower.select("span").text());
		/////////
		// System.out.println("====User+Time published====");
		Elements profile_project2 = doc_des.select("section.tile.created-by-tile");
		// //System.out.println(profile_project2);
		Elements user_time = profile_project2.select("section.media-body");
		// System.out.println(user_time.select("a.media-heading.tile-author").text());
		User = user_time.select("a.media-heading.tile-author").text();
		// System.out.println(user_time.select("time").text());
		String DATE_PUBLISH = user_time.select("time").text();
		Date_project = DATE_PUBLISH;

		String dateString = DATE_PUBLISH + " 00:00:00 GMT";
		DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss z");
		Date date = dateFormat.parse(dateString);
		long unixTime = (long) date.getTime() / 1000;
		// System.out.println("Unix timestamp: " + unixTime); // UNIX TIMESTAMP
		// FROM DATE STRING

		Document doc_update = Jsoup.connect("https://ideas.lego.com/projects/" + proj + "/updates").get();

		Elements update = doc_update.select("section.project-updates");
		// System.out.println("UPDATE: ");
		// System.out.println(update.text());

	}

	public static double wellBalanced(ArrayList<String> request, ArrayList<String> known) {

		// TODO Auto-generated constructor stub
		int q = request.size();
		int p = q + known.size();
		if (q >= p / 2) {
			return 2 * (p - q) / p;
		} else {
			return 2 * q / p;
		}

	}

	/**
	 * Calculate scope of change. According to Huang et al. [6], For a known
	 * term, KT, we define a function sp(KT) to represent scope of change, and
	 * it is usually pre-defined
	 * 
	 * @param String
	 *            string
	 * @return int
	 * @see scopeOfchange
	 */
	public static int scopeOfchange(String string) {

		return 1;

	}

	/**
	 * Calculate RELEVANCE score.
	 * 
	 * @param ArrayList<String>
	 *            request
	 * @param ArrayList<String>
	 *            known
	 * @return double
	 * @see relevanceScore
	 */
	public static double relevanceScore(ArrayList<String> request, ArrayList<String> known) {
		double Sum_trend = 0;
		double Sum_scope = 0;
		for (int i = 0; i < request.size(); i++) {
			try {
				Sum_trend = Sum_trend + TrendGI(request.get(i));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				// System.out.print("Error when calculate Trend");
			}

		}
		for (int i = 0; i < known.size(); i++) {
			Sum_scope = Sum_scope + scopeOfchange(known.get(i));
		}
		// System.out.println("===================");
		// System.out.print("Sum_trend: ");
		// System.out.println(Sum_trend);
		// System.out.print("Sum_scope: ");
		// System.out.println(Sum_scope);
		double c = 1.72;
		double b = wellBalanced(request, known);
		double rel = ((double) known.size() / request.size()) * ((double) Sum_trend / Sum_scope) * Math.log(c + b);
		return rel;

	}

	/**
	 * Calculate TrendGI basaed on {@link https://www.google.com/trends }
	 * 
	 * @param String
	 *            searching_word
	 * @return double
	 * @see TrendGI
	 */
	public static double TrendGI(String searching_word) throws IOException, IOException {

		// searching_word = searching_word.replaceAll(" ","%20");
		// System.out.println(searching_word);
		boolean check = true;
		String json1 = null;
		while (check) {
			String searching_word1 = searching_word.replaceAll(" ", "%20");
			String url = "http://www.google.com/trends/fetchComponent?hl=en-US&q=" + searching_word1
					+ "&cid=TIMESERIES_GRAPH_0&export=3";
			// System.out.println(url);
			json1 = IOUtils.toString(new URL(url));
			check = json1.contains("Not enough search volume to show results");
			if (searching_word.contains(" ")) {
				searching_word = searching_word.substring(0, searching_word.lastIndexOf(' '));
				// System.out.println(searching_word.lastIndexOf(" "));
				// System.out.println(searching_word);
			} else {
				break;
			}

		}

		// System.out.println(json1);
		json1 = removeCharAt(json1, 0, 61);
		json1 = removeCharAt(json1, json1.length() - 2, json1.length() - 1);
		// System.out.println(json1);
		String[] key_list = { "January 20", "February 20", "March 20", "April 20", "May 20", "June 20", "July 20",
				"August 20", "September 20", "October 20", "November 20", "December 20" };
		int sum = 0;
		int d = 0;
		for (int k = 4; k <= 15; k++) {
			NumberFormat formatter = new DecimalFormat("00");
			String s = formatter.format(k);
			String key;
			for (int i = 0; i < key_list.length; i++) {
				if (i == 0) {
					key = "December 20" + formatter.format(k - 1);
				} else {
					key = key_list[i - 1] + s;
				}

				String json = json1.substring(json1.indexOf("\"" + key + "\"") + key.length() + 9,
						json1.indexOf("\"" + key + "\"") + key.length() + 9 + 7);
				json = json.substring(0, json.indexOf(",") - 2);
				 System.out.print(key_list[i] + s + ": ");
				 System.out.println(json);
				if (k == 14) {
					sum = sum + Integer.parseInt(json);
					d = d + 1;
				}

				// //System.out.println(num);

			}

		}
		// System.out.println((double) sum / d);
		return (double) sum / d;

	}

	/**
	 * Remove a part of string from index pos_start to pos_end
	 * 
	 * @param String
	 *            s, int pos_start, int post_end
	 * @param String
	 *            s, int pos_start, int post_end
	 * @return String
	 * @see removeCharAt
	 */
	public static String removeCharAt(String s, int pos_start, int post_end) {
		return s.substring(0, pos_start) + s.substring(post_end + 1);
	}

	public static double diversityDegree(ArrayList<String> proportion) {
		Set<String> unique = new HashSet<String>(proportion);
		int Sum_id = proportion.size();
		double diversity = 0;
		for (String key : unique) {
			double pro;
			pro = (double) Collections.frequency(proportion, key) / Sum_id;
			// System.out.println(key + " proportion: " + pro);
			diversity = diversity + pro * ((double) Math.log(pro) / Math.log(2));
		}
		if (Sum_id < 1) {
			return -1;
		} else {
			return 0 - diversity;
		}

	}

	public static double concernDegree(int no_comment, long init, long end) {

		double days = (double) (end - init) / (24 * 3600);
		double concern = (double) days / (no_comment - 1);
		if (no_comment <= 1) {
			return -1;
		} else {
			return concern;
		}

	}

	// Formats a double to a String with SI units
	public static String format(double d) {
		String result = String.valueOf(d);
		// Get the prefix to use
		int prefixIndex = (int) (Math.log10(d) / Math.log10(10)) / 3;
		// We only have a limited number of prefixes
		if (prefixIndex > prefixes.length)
			prefixIndex = prefixes.length;
		// Divide the input to the appropriate prefix and add the prefix
		// character on the end
		if (prefixIndex > 0)
			result = String.valueOf(d / Math.pow(10, prefixIndex * 3)) + prefixes[prefixIndex - 1];
		// Return result
		return result;
	}

	// Parses a String formatted with SI units to a double
	public static double parse(String s) {
		// Retrieve the double part of the String (will throw an exception if
		// not a double)
		// double result = Double.parseDouble(s.substring(0, s.length() - 1));
		// Retrieve the prefix index used

		if (s.contains("k") || s.contains("M") || s.contains("G")) {
			double result = Double.parseDouble(s.substring(0, s.length() - 1));
			int prefixIndex = Arrays.asList(prefixes).indexOf(s.substring(s.length() - 1)) + 1;
			// Multiply the input to the appropriate prefix used
			if (prefixIndex > 0)
				result = result * Math.pow(10, prefixIndex * 3);
			return result;
		} else {
			return Double.parseDouble(s);
		}

	}

	public static ArrayList<String> extractLeaderuser() throws Exception {
		// String proj="101885";
		Document doc_community = Jsoup.connect("https://ideas.lego.com/community").get();
		ArrayList<String> leader = new ArrayList<String>();
		Elements leaderboard = doc_community.select("ul.items.unstyled");
		Elements leader_list = leaderboard.select("li");
		// System.out.println("=========Leader========");
		for (final Element li : leader_list) {
			Elements lis = li.select("h2");
			leader.add(lis.text());
			// System.out.print("Leader: ");
			// System.out.println(lis.text());
		}

		return leader;

	}

	public static void main(String[] args) throws Exception {
		Lego_CommentsFullmeasure list_project = new Lego_CommentsFullmeasure();
		ArrayList<String> id_list = new ArrayList<String>();
		// id_list =list_project.loadWordsList("C:/Users/Quan/workspace/Lego_CommentsMeasure/wordlist/input_Gather");
		// id_list =list_project.loadWordsList("C:/Users/Quan/workspace/Lego_CommentsMeasure/wordlist/input_Achieved");
		// id_list = list_project.loadWordsList("C:/Users/Quan/workspace/Lego_CommentsMeasure/wordlist/input_review");
		// id_list = list_project.loadWordsList("C:/Users/Quan/workspace/Lego_CommentsMeasure/wordlist/input_project");
		// id_list = list_project.loadWordsList("C:/Users/Quan/workspace/Lego_CommentsMeasure/wordlist/input_approved");
		    id_list = list_project.loadWordsList("C:/Users/Quan/workspace/Lego_CommentsMeasure/wordlist/input_NOTapproved");
		String[] id_project = new String[id_list.size()];
		id_project = id_list.toArray(id_project);

		ArrayList<output_data> Training_Data = new ArrayList<output_data>();

		for (int i = 0; i < id_project.length; i = i + 2) {
			System.out.println("===>" + i);
			Training_Data.add(list_project.trainingData(id_project[i], id_project[i + 1]));
		}

		Gson gson_measure = new Gson();
		String json_measure = gson_measure.toJson(Training_Data);
		// System.out.println(json_measure);
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
		Calendar cal = Calendar.getInstance();
		String mynamefile_measure = "TrainingData" + dateFormat.format(cal.getTime()) + ".txt";
		// String
		// mynamefileTotal="InfluenceTotal_quirky"+dateFormat.format(cal.getTime())+".txt";
		try {
			FileWriter writer_measure = new FileWriter(mynamefile_measure);
			writer_measure.write(json_measure);
			writer_measure.close();
			System.out.println("Write Tarining data: Success");

		} catch (Exception e) {
			// TODO: handle exception

		}

	}

}
