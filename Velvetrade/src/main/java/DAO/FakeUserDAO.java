package DAO;

import Model.Posting;
import Model.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository("UserDAO")
public class FakeUserDAO implements UserDAO {
    @Override
    public int addNewUser(User user) {
        try {
            Firestore dbFirestore = FirestoreClient.getFirestore();
            ApiFuture<WriteResult> ndoc = dbFirestore.collection("Users").document(user.getId()).set(user);
        }catch (Exception e){
            return 0;
        }
        return 1;
    }

    @Override
    public List<Posting> getAllPostingsPerUser(String id) {

        List<Posting> o=new ArrayList<>();
        Firestore dbFirestore = FirestoreClient.getFirestore();

        try {
            QuerySnapshot qs=dbFirestore.collectionGroup("Postings").whereEqualTo("userId",id).get().get();
            o=qs.toObjects(Posting.class);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        return o;
    }

    @Override
    public int updateUserByID(String id,User user) {
        try {
            Firestore dbFirestore = FirestoreClient.getFirestore();
            ApiFuture<WriteResult> ndoc = dbFirestore.collection("Users").document(user.getId()).set(user);
        }catch (Exception e){
            return 0;
        }
        return 1;
    }

    @Override
    public int deleteUserByID(String id) {
        try {
            Firestore dbFirestore = FirestoreClient.getFirestore();
            ApiFuture<WriteResult> ndoc = dbFirestore.collection("Users").document(id).delete();
        }catch (Exception e){
            return 0;
        }

        return 1;
    }

    @Override
    public User getUserByID(String id) {
        DocumentReference dr=FirestoreClient.getFirestore().collection("Users").document(id);
        ApiFuture<DocumentSnapshot> future = dr.get();
        try {
            DocumentSnapshot ds=future.get();
            if(ds.exists()){
                return ds.toObject(User.class);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> findUserByName(String name) {
        CollectionReference cr=FirestoreClient.getFirestore().collection("Users");
        Query q= cr.whereEqualTo("name",name);
        ApiFuture<QuerySnapshot> a = q.get();
        List<User> users=new ArrayList<>();
        try {
            for(DocumentSnapshot ds:a.get().getDocuments()){
                users.add(ds.toObject(User.class));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return users;
    }
}
