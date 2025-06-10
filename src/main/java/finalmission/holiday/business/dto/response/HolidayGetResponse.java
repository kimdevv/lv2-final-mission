package finalmission.holiday.business.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.ArrayList;
import java.util.List;

public class HolidayGetResponse {

    private Response response;

    public Response getResponse() {
        return response;
    }

    public List<Response.Body.Items.HolidayInfo> getHolidayInfo() {
        if (response.body.items == null) {
            return new ArrayList<>();
        }
        return response.body.items.item;
    }

    public static class Response {

        private Header header;
        private Body body;

        public Header getHeader() {
            return header;
        }

        public Body getBody() {
            return body;
        }

        public static class Header {
            private String resultCode;
            private String resultMsg;

            public String getResultCode() {
                return resultCode;
            }

            public String getResultMsg() {
                return resultMsg;
            }
        }

        public static class Body {

            private Items items;
            private int numOfRows;
            private int pageNo;
            private int totalCount;

            public Items getItems() {
                return items;
            }

            public int getNumOfRows() {
                return numOfRows;
            }

            public int getPageNo() {
                return pageNo;
            }

            public int getTotalCount() {
                return totalCount;
            }

            public static class Items {

                @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
                private List<HolidayInfo> item;

                public List<HolidayInfo> getItem() {
                    return item;
                }

                public static class HolidayInfo {

                    private String dateKind;
                    private String dateName;
                    private String isHoliday;
                    private String locdate;
                    private int seq;

                    public String getDateName() {
                        return dateName;
                    }

                    public String getLocdate() {
                        return locdate;
                    }

                    public String getDateKind() {
                        return dateKind;
                    }

                    public String getIsHoliday() {
                        return isHoliday;
                    }

                    public int getSeq() {
                        return seq;
                    }
                }
            }
        }
    }
}