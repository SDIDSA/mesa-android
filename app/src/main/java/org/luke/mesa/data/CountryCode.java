package org.luke.mesa.data;

import androidx.annotation.NonNull;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.utils.DataUtils;
import org.luke.mesa.abs.utils.ErrorHandler;
import org.luke.mesa.abs.utils.Platform;
import org.luke.mesa.abs.utils.functional.ObjectConsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CountryCode {
    private final String shortName;
    private final String name;
    private final String code;

    public CountryCode(String name, String shortName, String code) {
        this.name = name;
        this.shortName = shortName;
        this.code = code;
    }

    public static void searchAmong(App owner, String match, ObjectConsumer<List<CountryCode>> onResult) {
        searchAmong(DataUtils.readCountryCodes(owner), match, onResult);
    }

    public static void searchAmong(List<CountryCode> list, String match, ObjectConsumer<List<CountryCode>> onResult) {
        new Thread(() -> {
            ArrayList<ScoredCode> res = new ArrayList<>();
            for (CountryCode code : list) {
                int score = code.match(match);
                if (score > 0) {
                    res.add(new ScoredCode(code, score));
                }
            }
            res.sort((o1, o2) -> -Integer.compare(o1.score, o2.score));
            Platform.runLater(() -> {
                try {
                    onResult.accept(res.stream().map(sc -> sc.code).collect(Collectors.toList()));
                } catch (Exception x) {
                    ErrorHandler.handle(x, "handling search result for country code [ " + match + " ]");
                }
            });
        }).start();
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public String getCode() {
        return code;
    }

    public int match(String match) {
        match = match.replace("+", "");
        return matchAgainstName(match) + matchAgainstCode(match) + matchAgainstShortName(match);
    }

    private int matchAgainstName(String match) {
        return matchAgainst(match, name);
    }

    private int matchAgainstCode(String match) {
        return matchAgainst(match, code.replace("+", ""));
    }

    private int matchAgainstShortName(String match) {
        return matchAgainst(match, shortName);
    }

    private int matchAgainst(String match, String against) {
        if (match.trim().equalsIgnoreCase(against.trim())) {
            return 100;
        }
        String lowerMatch = match.toLowerCase();
        String lowerAgainst = against.toLowerCase();
        int index = -1;
        int score = 1;
        for (int mpos = 0; mpos < lowerMatch.length(); mpos++) {
            char c = lowerMatch.charAt(mpos);
            int pos = lowerAgainst.indexOf(c, index + 1);
            if (pos == -1) {
                return 0;
            } else {
                index = pos;
                if (pos == mpos) {
                    score += 3;
                } else if (pos == 0 || lowerAgainst.charAt(pos - 1) == ' ') {
                    score += 5;
                }
            }
        }
        return score;
    }

    @NonNull
    @Override
    public String toString() {
        return "CountryCode [name=" + name + ", code=" + code + "]";
    }

    static class ScoredCode {
        CountryCode code;
        int score;

        public ScoredCode(CountryCode code, int score) {
            this.code = code;
            this.score = score;
        }
    }
}
