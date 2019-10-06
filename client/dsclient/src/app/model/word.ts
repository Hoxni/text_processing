export class Word {
  word: string;
  frequency: bigint;

  constructor({word, frequency}){
    this.word = word;
    this.frequency = frequency;
  }

}
