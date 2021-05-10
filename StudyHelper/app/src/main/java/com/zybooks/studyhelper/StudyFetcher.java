package com.zybooks.studyhelper;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class StudyFetcher {

    public interface OnStudyDataReceivedListener {
        void onSubjectsReceived(List<Subject> subjects);
        void onQuestionsReceived(Subject subject, List<Question> questions);
        void onErrorResponse(VolleyError error);
    }

    private final String WEBAPI_BASE_URL = "https://wp.zybooks.com/study-helper.php";
    private final String TAG = "StudyFetcher";

    private RequestQueue mRequestQueue;


    public StudyFetcher(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public void fetchSubjects(final OnStudyDataReceivedListener listener) {

        String url = Uri.parse(WEBAPI_BASE_URL).buildUpon()
                .appendQueryParameter("type", "subjects").build().toString();

        // Request all subjects
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        List<Subject> subjects = parseJsonSubjects(response);
                        listener.onSubjectsReceived(subjects);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onErrorResponse(error);
                    }
                });

        mRequestQueue.add(request);
    }

    private List<Subject> parseJsonSubjects(JSONObject json) {

        // Create a list of subjects
        List<Subject> subjects = new ArrayList<>();

        try {
            JSONArray subjectArray = json.getJSONArray("subjects");

            for (int i = 0; i < subjectArray.length(); i++) {
                JSONObject subjectObj = subjectArray.getJSONObject(i);

                Subject subject = new Subject(subjectObj.getString("subject"));
                subject.setUpdateTime(subjectObj.getLong("updatetime"));
                subjects.add(subject);
            }
        }
        catch (Exception e) {
            Log.e(TAG, "One or more fields not found in the JSON data");
        }

        return subjects;
    }

    public void fetchQuestions(final Subject subject, final OnStudyDataReceivedListener listener) {

        String url = Uri.parse(WEBAPI_BASE_URL).buildUpon()
                .appendQueryParameter("type", "questions")
                .appendQueryParameter("subject", subject.getText())
                .build().toString();

        // Request questions for this subject
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        List<Question> questions = parseJsonQuestions(response);
                        listener.onQuestionsReceived(subject, questions);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onErrorResponse(error);
                    }
                });

        mRequestQueue.add(jsObjRequest);
    }

    private List<Question> parseJsonQuestions(JSONObject json) {

        // Create a list of questions
        List<Question> questions = new ArrayList<>();

        try {
            JSONArray questionArray = json.getJSONArray("questions");

            for (int i = 0; i < questionArray.length(); i++) {
                JSONObject questionObj = questionArray.getJSONObject(i);

                Question question = new Question();
                question.setText(questionObj.getString("question"));
                question.setAnswer(questionObj.getString("answer"));
                question.setSubjectId(0);
                questions.add(question);
            }
        }
        catch (Exception e) {
            Log.e(TAG, "One or more fields not found in the JSON data");
        }

        return questions;
    }
}