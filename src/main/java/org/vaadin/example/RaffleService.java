package org.vaadin.example;

import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RaffleService implements Serializable {

    Stream<String> getWinners() {
        List<String> participants = getParticipants().collect(Collectors.toList());
        Collections.shuffle(participants);
        return participants.stream().limit(getNumberOfWinners());
    }

    Stream<String> getParticipants() {
        return Stream.of(
                "Wil Higgs",
                "Amiya Parkinson",
                "Janae Sanderson",
                "Nela Bruce",
                "India Heath",
                "Roza Thomas",
                "Mariya Mitchell",
                "Abdur Witt",
                "Maegan Wang",
                "Jasleen Needham",
                "Maaria Gale",
                "Ruby-May Sexton",
                "Seamus Ochoa",
                "Tallulah Estes",
                "Arnie Zavala",
                "Abraham Benton",
                "Elli Carney",
                "Zakariyya Roberson",
                "Miles Sears",
                "Lily-May O'Neill",
                "Hashim Ramsey",
                "Rikki Mcdowell",
                "Caine Love",
                "Bronwen Hunt",
                "Keziah Bautista",
                "Jolene Glenn",
                "Isha Mullins",
                "Thierry Marsh",
                "Scott Serrano",
                "Junayd Yates",
                "Reiss Blanchard",
                "Caitlin Noel",
                "Aairah Wilde",
                "Dan Villanueva",
                "Ellie-Louise O'Sullivan",
                "Dafydd Haynes",
                "Safa Hooper",
                "Jacqueline Dalby",
                "Donnell Grant",
                "Chester Juarez",
                "Lidia Burns",
                "Jena Avila",
                "Kay Johns",
                "Hector Martin",
                "Taslima Kirk",
                "Malakai Singleton",
                "Julia Simmons",
                "Julie Torres",
                "Nishat Valenzuela",
                "Malaki Bean",
                "Alisha Buchanan",
                "Mai Burks",
                "Mario Clark",
                "Cayden Vazquez",
                "Amelia-Lily Lugo",
                "Rikesh Knox",
                "Jaydon Vu",
                "Kyal Thorne",
                "Azeem Gomez",
                "Monica Joyner",
                "Alec Lynch",
                "Mateusz Cobb",
                "Kady Clements",
                "Vinnie Rossi",
                "Farah Lynn",
                "Abdurahman Neville",
                "Madeline Watson",
                "Julius Schwartz",
                "Danny Herman",
                "Kaila Dennis",
                "Katherine Mclean",
                "Derry Benson",
                "Ronald Williams",
                "Kaylem Mcdermott",
                "Tracy Farmer",
                "Umar Fry",
                "Rhiannan Field",
                "Kya Prosser",
                "Helen Fischer",
                "Olly Wormald",
                "Anika Friedman",
                "Laaibah Kay",
                "Marius George",
                "Eiliyah Solomon",
                "Ewan Chang",
                "Kaitlyn Bowman",
                "Kian Sheehan",
                "Sophie Storey",
                "Jensen Smyth",
                "Alexia Cross",
                "Katelyn Davila",
                "Eilidh Perry",
                "Cora Hayward",
                "Lewie O'Quinn",
                "Isaac Roy",
                "Rae Fritz",
                "Subhan Alston",
                "Antonia Lindsay",
                "Tarun Mejia",
                "Joel Caldwell");
    }

    int getNumberOfWinners() {
        return 2;
    }

}
