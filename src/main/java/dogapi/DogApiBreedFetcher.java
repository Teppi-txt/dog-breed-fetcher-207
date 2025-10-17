package dogapi;

import okhttp3.*;
import okio.Timeout;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private final OkHttpClient client = new OkHttpClient();

    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */
    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        Request request = new Request.Builder().url("https://dog.ceo/api/breed/" + breed + "/list/").build();
        final Call call = client.newCall(request);
        try {
            Response response = call.execute();
            final JSONObject responseBody = new JSONObject(response.body().string());
            final JSONArray breedList = responseBody.getJSONArray("message");

            ArrayList<String> breeds = new ArrayList<>();

            for (int b_i = 0; b_i < breedList.length(); b_i++) {
                String breedString = breedList.getString(b_i);
                breeds.add(breedString);
            }
            return breeds;
        } catch (IOException | JSONException exception) {
            throw new BreedNotFoundException(breed);
        }

    }
}