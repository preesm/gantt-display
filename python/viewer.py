# Code written by K. Desnos
# With the help of Github Copilot - Claude Sonnet 3.7 Thinking
# Released under Cecill-C License

import xml.etree.ElementTree as ET
import matplotlib.pyplot as plt
import pandas as pd
import argparse
import mplcursors 

def create_gantt_chart(xml_file, output_file=None):
    """
    Create a Gantt chart from XML data showing events on their respective cores.
    Displays event name when hovering over a rectangle.
    
    Parameters:
    xml_file (str): Path to XML file containing event data
    output_file (str): Path to save the output image (optional)
    """
    try:
        # Parse the XML data from file
        root = ET.parse(xml_file).getroot()
        
        # Extract event data
        events = []
        for event in root.findall('event'):
            events.append({
                'title': event.get('title'),
                'start': int(event.get('start')),
                'end': int(event.get('end')),
                'color': event.get('color'),
                'mapping': event.get('mapping')
            })
        
        if not events:
            print(f"No events found in {xml_file}")
            return
            
        # Create a DataFrame
        df = pd.DataFrame(events)
        
        # Get unique mappings (cores)
        unique_mappings = df['mapping'].unique()
        
        # Plotting the Gantt chart with one line per core
        fig, ax = plt.subplots(figsize=(15, len(unique_mappings) * 0.5 + 2))
        
        # Create a dictionary to map mappings to y-positions
        mapping_positions = {mapping: i for i, mapping in enumerate(unique_mappings)}
        
        # Store references to the bars for hover annotation
        bars = []
        
        # Plot each event on its corresponding core line
        for i, (_, event) in enumerate(df.iterrows()):
            y_pos = mapping_positions[event['mapping']]
            bar = ax.barh(y_pos, 
                    width=event['end'] - event['start'], 
                    left=event['start'], 
                    color=event['color'],
                    edgecolor='black',
                    alpha=0.8,
                    height=0.8)
            # Store the bar with its index in the dataframe
            for rect in bar:
                rect.event_idx = i  # Attach the event index to the rectangle
            bars.extend(bar)  # Add individual rectangles to the bars list
        
        # Set y-ticks to show core names
        ax.set_yticks(range(len(unique_mappings)))
        ax.set_yticklabels(unique_mappings)
        
        # Add grid lines for better readability
        ax.grid(axis='x', linestyle='--', alpha=0.7)
        
        # Improve appearance
        ax.set_xlabel('Time')
        ax.set_ylabel('Core')
        ax.set_title('Gantt Chart')
        
        # Remove legend and add hover annotations
        cursor = mplcursors.cursor(bars, hover=True)
        
        @cursor.connect("add")
        def on_add(sel):
            # Get the event index directly from the rectangle
            if hasattr(sel.artist, 'event_idx'):
                event = df.iloc[sel.artist.event_idx]
                sel.annotation.set_text(f"Task: {event['title']}\nStart: {event['start']}\nEnd: {event['end']}\nDuration: {event['end'] - event['start']}")
        
        plt.tight_layout()
        
        # Save to file if output_file is provided
        if output_file:
            plt.savefig(output_file)
            print(f"Chart saved to {output_file}")
        
        plt.show()
        
    except FileNotFoundError:
        print(f"Error: File '{xml_file}' not found")
    except ET.ParseError:
        print(f"Error: '{xml_file}' is not a valid XML file")
    except Exception as e:
        print(f"Error: {e}")

def main():
    parser = argparse.ArgumentParser(description='Generate a Gantt chart from XML event data')
    parser.add_argument('input_file', help='Input XML file containing event data')
    parser.add_argument('-o', '--output', help='Output image file (e.g., gantt_chart.png)')
    
    args = parser.parse_args()
    
    create_gantt_chart(args.input_file, args.output)

if __name__ == "__main__":
    main()
