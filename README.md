# Flow Log Tagging System

This is a Java-based program that processes flow log data and tags each log entry based on a lookup table. The program is designed to support only the default log format and version 2 logs. 

<a name="readme-top"></a>



## Members
- Chongyuan Zhang




## About The Project

The objective of this project is to parse flow log data and assign tags to each log entry based on predefined rules specified in a lookup table. The system supports only the default log format and version 2 logs, ensuring that each log entry is mapped to the appropriate tag(s).



### Built With
- Java
- Visual Studio Code



## Getting Started

To set up this project locally, follow these instructions.

### Prerequisites
- Java Development Kit (JDK) 8 or higher.
- Any Java IDE (e.g., IntelliJ IDEA, Eclipse).

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/flow-log-tagging-system.git

2. Navigate to the project directory:
    ```bash
    cd FlowLogParser

3. Compile the Java files:
    ```bash
    javac FlowLogParser.java

4. Run the program:
    ```bash
    java FlowLogParser

## Usage

1. Place the flow_logs.txt and lookup_table.csv files in the project root directory.
2. Run the program using the command specified in the Installation section.
3. The program will output the results to the console.



## Output

The program generates two sets of statistics:
  1. Tag Counts: Displays how many times each tag appears in the flow logs.
  2. Port/Protocol Combination Counts: Shows the frequency of each port and protocol combination.




## Limitations

1. The program only processes version 2 logs in the default format.
2. Custom log formats are not supported.
3. The lookup table can have up to 10,000 mappings.



## Contact
Chongyuan Zhang - [chongyuanzhang.kris@gmail.com]
Project Link: [https://github.com/ThomasKris25]

