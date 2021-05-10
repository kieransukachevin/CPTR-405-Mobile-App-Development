package com.zybooks.studyhelper;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface QuestionDao {
    @Query("SELECT * FROM Question WHERE id = :id")
    public Question getQuestion(long id);

    @Query("SELECT * FROM Question WHERE subjectId = :subjectId ORDER BY id")
    public List<Question> getQuestions(long subjectId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertQuestion(Question question);

    @Update
    public void updateQuestion(Question question);

    @Delete
    public void deleteQuestion(Question question);
}