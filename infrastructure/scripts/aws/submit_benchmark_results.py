#!/usr/bin/env python3

import argparse
import pprint

parser = argparse.ArgumentParser(description='Submit a benchmark.')

parser.add_argument('benchmark_dir', help='Directory containing a benchmark result')

args = parser.parse_args()

benchmark_dir = args.benchmark_dir

