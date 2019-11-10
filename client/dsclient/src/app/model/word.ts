export class Word {
  word: string;
  frequency: bigint;
  tags: string[];
  lemma: string;

  constructor({word, frequency, tags, lemma}){
    this.word = word;
    this.frequency = frequency;
    this.tags = tags;
    this.lemma = lemma;
  }

}
