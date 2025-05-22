package pt.exercicio;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.concurrent.ConcurrentHashMap;

@Path("/labseq")
@Produces(MediaType.TEXT_PLAIN)
@Tag(name = "Labseq Sequence API", description = "Exercice from Altice Labs.\nEndpoints for calculating the labseq sequence values")
public class LabseqResource {

    private final ConcurrentHashMap<Integer, BigInteger> cache = new ConcurrentHashMap<>();

    @GET
    @Path("/{n}")
    @Operation(
            summary = "Calculates the value of l(n) from the labseq sequence",
            description = "Uses an iterative algorithm with in-memory caching for high performance"
    )
    @APIResponse(responseCode = "200", description = "Value of l(n) successfully calculated")
    @APIResponse(responseCode = "400", description = "Invalid input: n must be >= 0")
    public String labseqIterativoBig(@PathParam("n") int n) {
        long startTime = System.currentTimeMillis();
        if (n < 0) throw new IllegalArgumentException("n deve ser >= 0");

        // Initializes the first values if they are not already in the cache
        cache.putIfAbsent(0, BigInteger.ZERO);
        cache.putIfAbsent(1, BigInteger.ONE);
        cache.putIfAbsent(2, BigInteger.ZERO);
        cache.putIfAbsent(3, BigInteger.ONE);

        // Already exists?
        BigInteger res = cache.get(n);
        if (res != null) {
            long totalTime = System.currentTimeMillis() - startTime;
            return MessageFormat.format("Result directily from cache:\n{0}\n\nElapsed time: {1}ms", cache.get(n).toString(), totalTime);
        }

        // n not exists, but what about n - 3 and n - 4?
        BigInteger n_minus_3 = cache.get(n - 3);
        BigInteger n_minus_4 = cache.get(n - 4);
        if (n_minus_3 != null && n_minus_4 != null) {
            cache.putIfAbsent(n, cache.get(n - 4).add(cache.get(n - 3)));
            long totalTime = System.currentTimeMillis() - startTime;
            return MessageFormat.format("Result calculated from cache with previous key-values pair:\n{0}\n\nElapsed time: {1}ms", cache.get(n).toString(), totalTime);
        }

        for (int i = 4; i <= n; i++) {
            cache.putIfAbsent(i, cache.get(i - 4).add(cache.get(i - 3)));
        }

        String result = cache.get(n).toString();
        long totalTime = System.currentTimeMillis() - startTime;

        return MessageFormat.format("Calculated result:\n{0}\n\nElapsed time: {1}ms", result, totalTime);
    }

    /**
     * Auxiliar function to validate the cache size.
     * @return
     */
    public boolean isCacheTooBig() {
        return cache.size() > 500;
    }

    @GET
    @Path("/size")
    @Operation(
            summary = "Returns the current size of the cache",
            description = "Helps monitor how many labseq values are cached in memory"
    )
    @APIResponse(responseCode = "200", description = "Cache size returned successfully")
    public String getCacheSize() {
        return "Cache size: " + cache.size();
    }

    @GET
    @Path("/printCache")
    @Operation(
            summary = "Returns the current cache key.value pairs",
            description = "Helps monitor and see the real map of values"
    )
    @APIResponse(responseCode = "200", description = "Cache returned successfully")
    @APIResponse(responseCode = "400", description = "Invalid input: n must be > 500")
    public String printCache() {
        if (isCacheTooBig()) throw new WebApplicationException(
                Response.status(Response.Status.BAD_REQUEST)
                        .entity("Cache too big to print!")
                        .type(MediaType.TEXT_PLAIN)
                        .build()
        );

        return cache.toString();
    }

    @GET
    @Path("/clearCache")
    @Operation(
            summary = "Clears the cache",
            description = "Clears the cache to empty key-value map"
    )
    @APIResponse(responseCode = "200", description = "Cache has been cleared!")
    public String clearCache() {
        cache.clear();

        return "Cache has been cleared!";
    }
}
