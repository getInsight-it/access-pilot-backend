package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


public class TimePolicyRepresentationDTO {
    @JsonProperty("type")
    private String type = null;

    @JsonProperty("notBefore")
    private String notBefore = null;

    @JsonProperty("notOnOrAfter")
    private String notOnOrAfter = null;

    @JsonProperty("dayMonth")
    private String dayMonth = null;

    @JsonProperty("dayMonthEnd")
    private String dayMonthEnd = null;

    @JsonProperty("month")
    private String month = null;

    @JsonProperty("monthEnd")
    private String monthEnd = null;

    @JsonProperty("year")
    private String year = null;

    @JsonProperty("yearEnd")
    private String yearEnd = null;

    @JsonProperty("hour")
    private String hour = null;

    @JsonProperty("hourEnd")
    private String hourEnd = null;

    @JsonProperty("minute")
    private String minute = null;

    @JsonProperty("minuteEnd")
    private String minuteEnd = null;

    public TimePolicyRepresentationDTO type(String type) {
        this.type = type;
        return this;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TimePolicyRepresentationDTO notBefore(String notBefore) {
        this.notBefore = notBefore;
        return this;
    }


    public String getNotBefore() {
        return notBefore;
    }

    public void setNotBefore(String notBefore) {
        this.notBefore = notBefore;
    }

    public TimePolicyRepresentationDTO notOnOrAfter(String notOnOrAfter) {
        this.notOnOrAfter = notOnOrAfter;
        return this;
    }


    public String getNotOnOrAfter() {
        return notOnOrAfter;
    }

    public void setNotOnOrAfter(String notOnOrAfter) {
        this.notOnOrAfter = notOnOrAfter;
    }

    public TimePolicyRepresentationDTO dayMonth(String dayMonth) {
        this.dayMonth = dayMonth;
        return this;
    }


    public String getDayMonth() {
        return dayMonth;
    }

    public void setDayMonth(String dayMonth) {
        this.dayMonth = dayMonth;
    }

    public TimePolicyRepresentationDTO dayMonthEnd(String dayMonthEnd) {
        this.dayMonthEnd = dayMonthEnd;
        return this;
    }


    public String getDayMonthEnd() {
        return dayMonthEnd;
    }

    public void setDayMonthEnd(String dayMonthEnd) {
        this.dayMonthEnd = dayMonthEnd;
    }

    public TimePolicyRepresentationDTO month(String month) {
        this.month = month;
        return this;
    }


    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public TimePolicyRepresentationDTO monthEnd(String monthEnd) {
        this.monthEnd = monthEnd;
        return this;
    }


    public String getMonthEnd() {
        return monthEnd;
    }

    public void setMonthEnd(String monthEnd) {
        this.monthEnd = monthEnd;
    }

    public TimePolicyRepresentationDTO year(String year) {
        this.year = year;
        return this;
    }


    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public TimePolicyRepresentationDTO yearEnd(String yearEnd) {
        this.yearEnd = yearEnd;
        return this;
    }


    public String getYearEnd() {
        return yearEnd;
    }

    public void setYearEnd(String yearEnd) {
        this.yearEnd = yearEnd;
    }

    public TimePolicyRepresentationDTO hour(String hour) {
        this.hour = hour;
        return this;
    }


    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public TimePolicyRepresentationDTO hourEnd(String hourEnd) {
        this.hourEnd = hourEnd;
        return this;
    }


    public String getHourEnd() {
        return hourEnd;
    }

    public void setHourEnd(String hourEnd) {
        this.hourEnd = hourEnd;
    }

    public TimePolicyRepresentationDTO minute(String minute) {
        this.minute = minute;
        return this;
    }


    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public TimePolicyRepresentationDTO minuteEnd(String minuteEnd) {
        this.minuteEnd = minuteEnd;
        return this;
    }


    public String getMinuteEnd() {
        return minuteEnd;
    }

    public void setMinuteEnd(String minuteEnd) {
        this.minuteEnd = minuteEnd;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TimePolicyRepresentationDTO timePolicyRepresentation = (TimePolicyRepresentationDTO) o;
        return Objects.equals(this.type, timePolicyRepresentation.type) &&
                Objects.equals(this.notBefore, timePolicyRepresentation.notBefore) &&
                Objects.equals(this.notOnOrAfter, timePolicyRepresentation.notOnOrAfter) &&
                Objects.equals(this.dayMonth, timePolicyRepresentation.dayMonth) &&
                Objects.equals(this.dayMonthEnd, timePolicyRepresentation.dayMonthEnd) &&
                Objects.equals(this.month, timePolicyRepresentation.month) &&
                Objects.equals(this.monthEnd, timePolicyRepresentation.monthEnd) &&
                Objects.equals(this.year, timePolicyRepresentation.year) &&
                Objects.equals(this.yearEnd, timePolicyRepresentation.yearEnd) &&
                Objects.equals(this.hour, timePolicyRepresentation.hour) &&
                Objects.equals(this.hourEnd, timePolicyRepresentation.hourEnd) &&
                Objects.equals(this.minute, timePolicyRepresentation.minute) &&
                Objects.equals(this.minuteEnd, timePolicyRepresentation.minuteEnd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, notBefore, notOnOrAfter, dayMonth, dayMonthEnd, month, monthEnd, year, yearEnd, hour, hourEnd, minute, minuteEnd);
    }

    @Override
    public String toString() {

        String sb = "class TimePolicyRepresentationDTO {\n" +
                "    type: " + toIndentedString(type) + "\n" +
                "    notBefore: " + toIndentedString(notBefore) + "\n" +
                "    notOnOrAfter: " + toIndentedString(notOnOrAfter) + "\n" +
                "    dayMonth: " + toIndentedString(dayMonth) + "\n" +
                "    dayMonthEnd: " + toIndentedString(dayMonthEnd) + "\n" +
                "    month: " + toIndentedString(month) + "\n" +
                "    monthEnd: " + toIndentedString(monthEnd) + "\n" +
                "    year: " + toIndentedString(year) + "\n" +
                "    yearEnd: " + toIndentedString(yearEnd) + "\n" +
                "    hour: " + toIndentedString(hour) + "\n" +
                "    hourEnd: " + toIndentedString(hourEnd) + "\n" +
                "    minute: " + toIndentedString(minute) + "\n" +
                "    minuteEnd: " + toIndentedString(minuteEnd) + "\n" +
                "}";
        return sb;
    }


    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}

