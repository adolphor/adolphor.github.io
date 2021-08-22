package Y2016.m07.d26;

/**
 * @Author: Bob.Zhu
 * @Date: 2021/8/22 19:54
 */
public class HttpRangeRequest {
  private final Range range;
  private String url;

  public HttpRangeRequest(String url, int lowerBound, int upperBound) {
    this.url = url;
    this.range = new Range(lowerBound, upperBound);
  }

  public Range getRange() {
    return range;
  }

  public static class Range {
    private long lowerBound;
    private long upperBound;

    public Range(long lowerBound, long upperBound) {
      this.lowerBound = lowerBound;
      this.upperBound = upperBound;
    }
  }

}
